package aoc.common.input

import java.net.URL

interface IAoCFileInput {
    val inputFileUrl: URL
        get() =
            with(this::class.java) {
                getResource("/${name.replace('.', '/').lowercase()}.txt")!!
            }

    fun inputFileIndexUrl(index: Int): URL =
        with(this::class.java) {
            getResource("/${name.replace('.', '/').lowercase()}$index.txt")!!
        }
}
