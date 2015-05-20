class UrlMappings {

    static mappings = {


        "/ws/search/$action"(controller: "search")

        "/keybase/keyJSON"(controller: "player", action: "index")

        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "home")
        "500"(view: '/error')
    }
}
