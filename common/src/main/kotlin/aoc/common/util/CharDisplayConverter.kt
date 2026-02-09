package aoc.common.util

import aoc.common.entity.CharConstants.CHAR_E
import aoc.common.entity.CharConstants.CHAR_F
import aoc.common.entity.CharConstants.CHAR_G
import aoc.common.entity.CharConstants.CHAR_H
import aoc.common.entity.CharConstants.CHAR_I
import aoc.common.entity.CharConstants.CHAR_J
import aoc.common.entity.CharConstants.CHAR_K
import aoc.common.entity.CharConstants.CHAR_O
import aoc.common.entity.CharConstants.CHAR_P
import aoc.common.entity.CharConstants.CHAR_R
import aoc.common.entity.CharConstants.CHAR_S
import aoc.common.entity.CharConstants.CHAR_Y
import aoc.common.entity.CharConstants.CHAR_Z
import aoc.common.exception.UnsupportedTypeException

fun getChar5on6(charDisplay: Int): Char =
    when (charDisplay) {
        504405039 -> CHAR_E
        34642991 -> CHAR_F
        479626534 -> CHAR_G
        311737641 -> CHAR_H
        474091662 -> CHAR_I
        211034380 -> CHAR_J
        307399849 -> CHAR_K
        211068198 -> CHAR_O
        34841895 -> CHAR_P
        307471655 -> CHAR_R
        243467310 -> CHAR_S
        138553905 -> CHAR_Y
        504434959 -> CHAR_Z
        else -> throw UnsupportedTypeException("This charCode: \"$charDisplay\" is not supported.")
    }
