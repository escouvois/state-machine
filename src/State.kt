package fr.bescouvois

data class State<S, out A>(val run: (S) -> Pair<A, S>) {
    fun <B> flatMap(f: (A) -> State<S, B>): State<S, B> =
        State {
            val (a, s1) = run(it)
            f(a).run(s1)
        }

    fun <B> map(f: (A) -> B): State<S, B> =
        flatMap { unit<S, B>(f(it)) }

    fun <B, C> map2(sb: State<S, B>, f: (A, B) -> C): State<S, C> =
        flatMap { a -> sb.map { b -> f(a, b) } }

    companion object {
        fun <S, A> unit(a: A): State<S, A> =
            State { Pair(a, it) }

        fun <S, A> sequence(l: List<State<S, A>>): State<S, List<A>> =
            l.foldRight(unit(emptyList())) { f: State<S, A>, acc: State<S, List<A>> ->
                f.map2(acc) { a: A, list: List<A> -> listOf(a) + list }
            }

        fun <S> get(): State<S, S> =
            State { Pair(it, it) }

        fun <S> set(s: S): State<S, Unit> =
            State { Pair(Unit, s) }

        fun <S> modify(f: (S) -> S): State<S, Unit> =
            get<S>().flatMap { set(f(it)) }
    }
}




