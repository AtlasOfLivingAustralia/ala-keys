class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/ws/search/$action" (controller: "search")

        "/"(controller: "home")
        "500"(view: '/error')
    }
}
