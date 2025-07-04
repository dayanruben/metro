@DependencyGraph(scope = AppScope::class, isExtendable = true)
interface AppGraph {
  @DependencyGraph.Factory
  interface Factory {
    fun create(@Includes serviceProvider: IntProvider): AppGraph
  }
}

@ContributesGraphExtension(scope = Unit::class)
interface ChildGraph {
  @ContributesGraphExtension.Factory(scope = AppScope::class)
  interface Factory {
    fun create(): ChildGraph
  }
}

interface IntProvider {
  fun int(): Int
}

fun box(): String {
  val graph =
    createGraphFactory<AppGraph.Factory>()
      .create(
        object : IntProvider {
          override fun int(): Int {
            return 3
          }
        }
      )
  val child = graph.create()
  assertNotNull(child)
  return "OK"
}
