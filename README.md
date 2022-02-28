<p align="center">
    <img src="https://user-images.githubusercontent.com/10709682/156048080-34987a54-d433-4931-acc9-0fc416b94f8b.png" />
</p>
Storm is a fast, easy to use, no-bullshit opinionated Java ORM inspired by Doctrine. The main goal of this project is to allow future-mats to easily implement Sql-based storage and relation solutions in my projects and easily prototype concepts, without harming production usability.

## Features
 - Automatic schema creation and updates based on models
 - Built in support for java types (Integer, String, Uuid, Boolean) with an API to add your own
 - Support for OneToMany mappings with arraylist columns
 - Out-of-the-box adapters for Sqlite (flat file), Sqlite (Memory) and HiariCP-MariaDB
 - Dynamic SQL Dialects depending on the target platform
 - Incredibly easy to use API

# Performance
Tests ran on my main workstation, targeting a Sqlite flatfile and memory database.
![image](https://user-images.githubusercontent.com/10709682/156046029-537cf0dd-fd3b-4a6e-ab6b-17bf832046d4.png)

# Examples
```java
// create a model
@Data
@Table(name = "user")
class User extends StormModel {

    @Column
    private String userName;

    @Column
    private Integer score;

    @Column(
            type = ColumnType.ONE_TO_MANY,
            references = {SocialPost.class},
            matchTo = "poster"
    )
    private List<SocialPost> posts;

    @Column
    private UUID minecraftUserId = UUID.randomUUID();

    @Column(
            name = "email",
            defaultValue = "default@craftmend.com"
    )
    private String emailAddress;

}

public class SocialPost extends StormModel {

    @Column(
            notNull = true
    )
    private String content;

    @Column(
            keyType = KeyType.FOREIGN,
            references = {User.class}
    )
    private Integer poster;

}


// create an instance
Storm storm = new Storm(new SqliteDriver(dataFile));
// register one table
storm.migrate(new User());
storm.migrate(new SocialPost());
storm.runMigrations();

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
