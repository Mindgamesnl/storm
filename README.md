# storm
Storm is a super simple to use, no bullshit MySql(ite) ORM with a query builder, support for type conversion, schema management and connection handling.


# Example
```java
// create a model
@Data
@Table(name = "user")
class User extends StormModel {

    @Column
    private String userName;

    @Column
    private Integer score;

    @Column
    private UUID minecraftUserId = UUID.randomUUID();

    @Column(
            name = "email",
            defaultValue = "default@craftmend.com"
    )
    private String emailAddress;

}

// create an instance
Storm storm = new Storm(new SqliteDriver(dataFile));
// register one table
storm.migrate(new User());

// create a new user
User mindgamesnl = new User();
mindgamesnl.setUserName("Mindgamesnl");
mindgamesnl.setEmailAddress("mats@toetmats.nl");
mindgamesnl.setScore(9009);

// save or update the user
storm.save(mindgamesnl);

// query for users
Collection<User> justMindgamesnl =
        storm.buildQuery(User.class)
        .where("user_name", Where.EQUAL, "Mindgamesnl")
        .limit(1)
        .execute()
        .join();
```

# Usage with HikariCP
```java
// hikari
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/simpsons");
config.setUsername("bart");
config.setPassword("51mp50n");
config.addDataSourceProperty("cachePrepStmts", "true");
config.addDataSourceProperty("prepStmtCacheSize", "250");
config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

Storm storm = new Storm(new HikariDriver(config));
```