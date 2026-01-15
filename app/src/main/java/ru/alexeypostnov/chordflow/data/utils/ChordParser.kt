package ru.alexeypostnov.chordflow.data.utils

import ru.alexeypostnov.chordflow.data.model.ChordLineModel
import ru.alexeypostnov.chordflow.data.model.ChordPosition

interface ChordParser {
    fun parseLine(line: String): ChordLineModel
    fun parseText(text: String): List<ChordLineModel>
    fun extractChords(text: String): List<String>
    fun isComment(text: String): Boolean
}

class ChordParserImpl: ChordParser {
    private val chordMask = Regex("""\[([^]]+)]""")
    private val commentMask = Regex("""^\s*//""")

    override fun parseLine(line: String): ChordLineModel {
        if (isComment(line)) {
            val commentText = line.substringAfter("//").trimStart()
            return ChordLineModel(
                chords = emptyList(),
                lyrics = commentText,
                isComment = true
            )
        }

        val chordMatches = chordMask.findAll(line)
        val chords = mutableListOf<ChordPosition>()

        val textWithoutCords = chordMask.replace(line, "")

        chordMatches.forEach { matchResult ->
            val chord = matchResult.groupValues[1]

            val textBefore = line.substring(0, matchResult.range.first)
            val cleanTextBefore = chordMask.replace(textBefore, "")

            chords.add(ChordPosition(chord, cleanTextBefore.length))
        }

        return ChordLineModel(chords, textWithoutCords, false)
    }

    override fun parseText(text: String): List<ChordLineModel> {
        return text.lines()
            .map { line ->
                if (line.isBlank()) {
                    ChordLineModel(emptyList(), "", false)
                } else {
                    parseLine(line)
                }
            }
    }

    override fun extractChords(text: String): List<String> {
        return chordMask.findAll(text)
            .map { it.groupValues[1] }
            .toList()
            .distinct()
    }

    override fun isComment(text: String): Boolean {
        return commentMask.containsMatchIn(text)
    }
}