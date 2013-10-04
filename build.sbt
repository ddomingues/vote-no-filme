name := "vote-no-filme"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)     

play.Project.playJavaSettings
