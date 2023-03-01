pluginManagement {
    repositories {
        maven {
            setName("Quilt")
            setUrl("https://maven.quiltmc.org/repository/release/")
        }
        maven {
            setName("Quilt Snapshot")
            setUrl("https://maven.quiltmc.org/repository/snapshot/")
        }
        maven {
            setName("Fabric")
            setUrl("https://maven.fabricmc.net/")
        }
        jcenter()
        maven {
            setName("Forge")
            setUrl("https://files.minecraftforge.net/maven/")
        }
        maven {
            setName("Jitpack")
            setUrl("https://jitpack.io/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.setName("Configurable Data Fixers")

localRepository("FrozenLib", "maven.modrinth:frozenlib")

fun localRepository(repo: String, dependencySub: String) {
    println("Attempting to include local repo $repo")

    val allowLocalRepoUse = false
    val allowLocalRepoInConsoleMode = true

    val androidInjectedInvokedFromIde by extra("android.injected.invoked.from.ide")
    val xpcServiceName by extra("XPC_SERVICE_NAME")
    val ideaInitialDirectory by extra("IDEA_INITIAL_DIRECTORY")

    val isIDE = androidInjectedInvokedFromIde != "" || (System.getenv(xpcServiceName) ?: "").contains("intellij") || (System.getenv(xpcServiceName) ?: "").contains(".idea") || System.getenv(ideaInitialDirectory) != null
    val github = System.getenv("GITHUB_ACTIONS") == "true"

    var path = "../$repo"
    var file = File(path)

    val prefixedRepoName = ":$repo"

    if (allowLocalRepoUse && (isIDE || allowLocalRepoInConsoleMode)) {
        if (github) {
            path = repo
            file = File(path)
            println("Running on GitHub")
        }
        if (file.exists()) {
            /*includeBuild(path) {
                dependencySubstitution {
                    if (dependencySub != "") {
                        substitute(module(dependencySub)).using(project(":"))
                    }
                }
            }*/
            include(prefixedRepoName)
            project(prefixedRepoName).projectDir = file
            project(prefixedRepoName).buildFileName = "./build.gradle.kts"
            println("Included local repo $repo")
        } else {
            println("Local repo $repo not found")
        }
    }
}
