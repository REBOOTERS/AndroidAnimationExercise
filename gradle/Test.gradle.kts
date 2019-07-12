fun calc_module() {
    println("this is kts ================hello")
    val proj: Project = project
    println("project ${proj.configurations}")
    proj.configurations.forEach { configuration ->
        println("configure==" + configuration.name)
        println("configure==" + configuration.hierarchy)
        println("")
        var config = configuration
    }

//    val configuration = proj.configurations.getByName("kotlinCompilerClasspath")
//    println("resolver ==" + configuration.resolvedConfiguration)
//    configuration.resolvedConfiguration.resolvedArtifacts.forEach { resolvedArtifact ->
//        println("resolve==" + resolvedArtifact.name)
//    }

}

calc_module()