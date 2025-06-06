// Regression test for https://github.com/ZacSweers/metro/issues/525
@DependencyGraph(AppScope::class, isExtendable = true)
interface AppGraph {

  @DependencyGraph.Factory
  interface Factory {
    fun create(@Includes stringProvider: StringProvider): AppGraph
  }
}

interface StringProvider {
  val str: String
}

@ContributesTo(AppScope::class)
interface TestModule {
  @Provides fun providesInt(str: String): Int = str.length
}

@ContributesGraphExtension(scope = Unit::class)
interface LoggedInGraph {

  val intLength: Int

  @ContributesGraphExtension.Factory(AppScope::class)
  interface Factory {
    fun createLoggedInGraph(): LoggedInGraph
  }
}

fun box(): String {
  val stringProvider = object : StringProvider {
    override val str: String = "Hello, world!"
  }
  val parent = createGraphFactory<AppGraph.Factory>().create(stringProvider)
  val child = parent.createLoggedInGraph()
  assertEquals(child.intLength, stringProvider.str.length)
  return "OK"
}
