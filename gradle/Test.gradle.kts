fun calc_module() {
    println("this is kts ================hello")
    val proj: Project = project
    println("project ${proj.configurations}")
    proj.configurations.forEach { configuration ->
        println("configure==" + configuration.name)
    }

    val configuration = proj.configurations.findByName("chargeDebugCompileClasspath")
    println("find it $configuration")

//    println("resolver ==" + configuration.resolvedConfiguration)
//    configuration.resolvedConfiguration.resolvedArtifacts.forEach { resolvedArtifact ->
//        println("resolve==" + resolvedArtifact.name)
//    }

}

calc_module()