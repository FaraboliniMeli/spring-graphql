db.createUser(
    {
        user: "graphQlAdmin",
        pwd: "graphQlAdmin",
        roles: [
            {
                role: "readWrite",
                db: "GraphQlCrud"
            }
        ]
    }
);