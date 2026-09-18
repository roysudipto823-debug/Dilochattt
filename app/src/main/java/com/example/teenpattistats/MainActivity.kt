package com.example.teenpattistats

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import kotlin.math.roundToInt

class MainActivity : Activity() {
    private val history = mutableListOf<String>()
    private lateinit var stats: TextView
    private lateinit var historyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
        update()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 28, 28, 28)
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(this).apply {
            text = "Teen Patti Stats"
            textSize = 28f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 20)
        }
        root.addView(title)

        val note = TextView(this).apply {
            text = "Past A/B/C results enter karke frequency aur streak dekho."
            textSize = 14f
            setPadding(0, 0, 0, 18)
        }
        root.addView(note)

        val buttons = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        listOf("A", "B", "C").forEach { value ->
            val button = Button(this).apply {
                text = value
                textSize = 20f
                setOnClickListener {
                    history.add(value)
                    update()
                }
            }
            buttons.addView(button, LinearLayout.LayoutParams(0, -2, 1f))
        }

        root.addView(buttons)

        val clear = Button(this).apply {
            text = "Clear History"
            setOnClickListener {
                history.clear()
                update()
            }
        }

        root.addView(clear)

        stats = TextView(this).apply {
            textSize = 17f
            setPadding(0, 20, 0, 20)
        }

        root.addView(stats)

        historyView = TextView(this).apply {
            textSize = 16f
        }

        root.addView(historyView)
        setContentView(root)
    }

    private fun update() {
        val total = history.size
        val a = history.count { it == "A" }
        val b = history.count { it == "B" }
        val c = history.count { it == "C" }

        fun pct(x: Int) =
            if (total == 0) 0 else (x * 100.0 / total).roundToInt()

        val streak = if (history.isEmpty()) {
            "—"
        } else {
            val last = history.last()
            var count = 0

            for (i in history.size - 1 downTo 0) {
                if (history[i] == last) count++ else break
            }

            "$last × $count"
        }

        stats.text = """
            Total rounds: $total

            A: $a (${pct(a)}%)
            B: $b (${pct(b)}%)
            C: $c (${pct(c)}%)

            Current streak: $streak
        """.trimIndent()

        historyView.text =
            "History (latest first):\n" +
            if (history.isEmpty()) "No results yet."
            else history.asReversed().joinToString("  ")
    }
}
