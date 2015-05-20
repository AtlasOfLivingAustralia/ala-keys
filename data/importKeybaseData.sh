#!/bin/bash

#mysql connection info
database=kb
user=root
#user=root -p user_password

keysUrl=http://local.ala.org.au:8080/ala-keys

#tmp files
projectList=/tmp/keys_projects.txt
projectJson=/tmp/keys_project.json
keyList=/tmp/keys_keys.txt
keyJson=/tmp/keys_key.json
keyText=/tmp/keys_key.txt
lsids=/tmp/lsids.txt

#export project ids
rm $projectList
mysql -e "SELECT projectsId FROM projects INTO OUTFILE '$projectList';" -u $user -D $database

#process each project
cat $projectList | while read pId
do
  echo processing project id:$pId

  #export project header
  rm $projectJson
  mysql -e "SELECT concat('{ \"name\": \"', ifnull(name,''), \
    '\", \"name\": \"', ifnull(name,''), \
	'\", \"description\": \"', replace(replace(ifnull(description,''),'\"','\''),'\n','') , \
    '\", \"scopeTaxons\": [\"', ifnull(taxonomicscope,'') ,'\"],\"geographicScope\" : \"',ifnull(geographicscope,''), \
	'\", \"imageUrl\": \"',ifnull(projecticon,''),'\"}') as txt FROM kb.projects  WHERE projectsId=$pId INTO OUTFILE '$projectJson';" -u $user -D $database

  #create project  
  projectId=$(curl --silent $keysUrl/project/create -XPOST -H "Content-type: application/json" -d "@$projectJson" | grep -o '"id":[0-9]*,')
  l1=${#projectId}
  projectId=${projectId:5:(l1-6)}
  echo $pId:$projectId
  
  #export key ids for this project
  rm $keyList
  mysql -e "SELECT keysId FROM $database.keys WHERE projectsId=$pId INTO OUTFILE '$keyList';" -u $user -D $database
  echo exported key list
  
  #process each key in this project
  cat $keyList | while read kId
  do
    echo processing key $keyText$kId.txt
     
    #export key text
    rm $keyText$kId.txt
    mysql -e "SELECT distinct b.nodenumber, replace(replace(replace(a.leadtext,'\t',' '),'\n',' '),'\"',''), \
    (case when c.leadtext is null then c.nodename else a.nodenumber end) \
    FROM kb.leads a left join kb.leads b on a.parentid = b.leadsid left join kb.leads c \
    on a.leadsid = c.parentid  where a.keysId=$kId and b.nodenumber is not null \
    and a.leadtext is not null order by b.nodenumber into outfile '$keyText$kId.txt';" -u $user -D $database

    if [ -s $keyText$kId.txt ]
    then 
    
    #import key
    keyId=$(curl --silent -H "Content-Type:multipart/form-data" -XPOST -F "file=@$keyText$kId.txt" -F "projectId=$projectId" $keysUrl/uploadItem/upload | grep -o '"id":[0-9]*,')
    l2=${#keyId}
    keyId=${keyId:5:(l2-6)}
    echo $pId:$projectId:$kId:$keyId
    
    #export key header
    rm $keyJson$kId
    mysql -e "SELECT concat('{ \"id\": $keyId, \"name\": \"', ifnull(name,''), \
      '\", \"geographicScope\": \"', ifnull(geographicScope,''), \
      '\", \"description\": \"', \
	  replace(replace((case when ifnull(description,'') <> '' then description else ifnull(notes,'') end),'\"','\''),'\n','') , \
	  '\", \"scopeTaxons\": [\"', ifnull(taxonomicscope,'') ,'\"]', \
	  (case when s.sourcesid is null then '' else \
	  	 concat(', \"metadata\": { ', \
'\"authors\" : \"', ifnull(s.authors,'') , '\",', \
'\"year\" : \"', ifnull(s.year,'') , '\",',\
'\"title\" : \"', replace(ifnull(s.title,''),'\n','') , '\",',\
'\"edition\" : \"', ifnull(s.edition,'') , '\",',\
'\"inauthors\" : \"', ifnull(s.inauthors,'') , '\",',\
'\"intitle\" : \"', replace(replace(replace(ifnull(s.intitle,''),'\"','\''),'\n',' '),'\t',' ') , '\",',\
'\"journal\" : \"', ifnull(s.journal,'') , '\",',\
'\"series\" : \"', ifnull(s.series,'') , '\",',\
'\"volume\" : \"', ifnull(s.volume,'') , '\",',\
'\"part\" : \"', ifnull(s.part,'') , '\",',\
'\"publisher\" : \"', ifnull(s.publisher,'') , '\",',\
'\"placeofpublication\" : \"', ifnull(s.placeofpublication,'') , '\",',\
'\"pages\" : \"', ifnull(s.pages,'') , '\",',\
'\"url\" : \"', ifnull(s.url,'') , '\",',\
'\"modified\" : \"', ifnull(s.modified,''), '\"',\
'}')\
	  end)\
	  ,'}') as txt \
	  FROM $database.keys LEFT JOIN sources s ON $database.keys.sourcesid = s.sourcesid \
	  WHERE keysId=$kId INTO OUTFILE '$keyJson$kId';"   -u $user -D $database
    
    #update key
    update=$(curl --silent -H "Content-Type:application/json" -d "@$keyJson$kId" -XPUT $keysUrl/key/update)
    echo key updated

	fi
  done
  
done

#get all lsids
mysql -e "SET SESSION group_concat_max_len = 10000000;select concat('[', group_concat(txt),']') from \
(select concat('{ \"lsid\" : \"',lsid, '\", \"scientificName\" : \"', name, '\" }') txt \
 from items where lsid is not null and lsid <> '') a INTO OUTFILE '$lsids';" -u $user -D $database

#import lsids
update=$(curl --silent -H "Content-Type:application/json" -d "@$lsids" -XPUT $keysUrl/taxon/setLsid)
echo lsids updated


