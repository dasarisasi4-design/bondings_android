package com.bondings.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(15, 10, 29)
    private val panel = Color.rgb(30, 20, 49)
    private val pink = Color.rgb(255, 65, 157)
    private val purple = Color.rgb(151, 84, 255)
    private val muted = Color.rgb(174, 160, 195)
    private lateinit var root: LinearLayout
    private lateinit var content: LinearLayout
    private var current = "Discover"

    data class Person(val name: String, val age: Int, val distance: String, val tag: String, val color: Int)
    private val people = listOf(
        Person("Aarav", 25, "1 km", "Online", 0xFF7544A8.toInt()),
        Person("Rohan", 27, "2 km", "Online", 0xFFB34D82.toInt()),
        Person("Vihaan", 24, "3 km", "New", 0xFF5A6BA8.toInt()),
        Person("Kabir", 29, "4 km", "Online", 0xFF9B5B55.toInt()),
        Person("Ishaan", 26, "5 km", "New", 0xFF4D8B83.toInt()),
        Person("Dev", 28, "6 km", "Online", 0xFF7955B4.toInt()),
        Person("Arjun", 23, "7 km", "Online", 0xFFB06C45.toInt()),
        Person("Neil", 30, "8 km", "New", 0xFF536E9B.toInt()),
        Person("Aditya", 26, "9 km", "Online", 0xFF9B4F76.toInt())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        buildShell()
        show("Discover")
    }

    private fun buildShell() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(20), dp(18), dp(20), dp(12))
        }
        val brand = TextView(this).apply {
            text = "BONDINGS"
            textSize = 23f
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
            setTextColor(Color.WHITE)
            letterSpacing = .08f
        }
        header.addView(brand, LinearLayout.LayoutParams(0, dp(42), 1f))
        val premium = TextView(this).apply {
            text = "✦ GO PREMIUM"
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(dp(14), dp(10), dp(14), dp(10))
            background = shape(purple, 24)
            setOnClickListener { show("Premium") }
        }
        header.addView(premium)
        root.addView(header)

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(6), dp(16), dp(12))
        }
        val scroll = ScrollView(this).apply { isFillViewport = true; addView(content) }
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(5), dp(8), dp(5), dp(10))
            setBackgroundColor(Color.rgb(22, 15, 37))
        }
        listOf("Discover", "Likes", "Chat", "Premium", "Profile").forEach { item ->
            val b = TextView(this).apply {
                text = when(item) { "Discover" -> "⌕\nExplore"; "Likes" -> "♡\nLikes"; "Chat" -> "▤\nChat"; "Premium" -> "✦\nPlans"; else -> "◉\nMe" }
                gravity = Gravity.CENTER
                textSize = 11f
                setTextColor(if (item == current) pink else muted)
                setPadding(dp(5), dp(3), dp(5), dp(3))
                setOnClickListener { show(item) }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, dp(52), 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun show(page: String) {
        current = page
        content.removeAllViews()
        when(page) {
            "Discover" -> discover()
            "Likes" -> simplePage("Your likes", "People you connect with will appear here.", "Discover people")
            "Chat" -> simplePage("Messages", "Your conversations will appear here when messaging is connected.", "Explore profiles")
            "Premium" -> premiumPage()
            "Profile" -> profilePage()
            else -> discover()
        }
    }

    private fun discover() {
        title("Discover", "Find your people. Build real connections.")
        val filter = TextView(this).apply {
            text = "⚙  Filters     •     Nearby  ⌄       Online  ●"
            textSize = 13f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
            setPadding(dp(12), dp(13), dp(12), dp(13)); background = shape(panel, 14)
            setOnClickListener { toast("Filters will be connected to live profiles in the next integration.") }
        }
        content.addView(filter, matchWrap(12))
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val count = TextView(this).apply { text = "NEAR YOU"; textSize = 11f; letterSpacing = .12f; typeface = Typeface.DEFAULT_BOLD; setTextColor(muted) }
        row.addView(count, LinearLayout.LayoutParams(0, dp(38), 1f))
        val online = TextView(this).apply { text = "● Online now"; textSize = 11f; setTextColor(Color.rgb(88, 220, 153)) }
        row.addView(online)
        content.addView(row)
        val grid = GridLayout(this).apply { columnCount = 3; useDefaultMargins = false }
        people.forEach { person ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(4), dp(4), dp(4), dp(8))
                setOnClickListener { profilePreview(person) }
            }
            val photo = FrameLayout(this).apply {
                background = shape(person.color, 15)
            }
            val initials = TextView(this).apply {
                text = person.name.take(1)
                textSize = 43f
                typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
            }
            photo.addView(initials, FrameLayout.LayoutParams(-1, -1))
            if(person.tag == "Online") {
                val dot = TextView(this).apply { text = "●"; textSize = 15f; setTextColor(Color.rgb(65, 235, 145)) }
                val dp = FrameLayout.LayoutParams(dp(24), dp(24), Gravity.TOP or Gravity.END)
                photo.addView(dot, dp)
            }
            card.addView(photo, LinearLayout.LayoutParams(-1, dp(124)))
            val nm = TextView(this).apply {
                text = "${person.name}, ${person.age}"
                textSize = 12f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.WHITE)
                maxLines = 1
            }
            card.addView(nm)
            val dist = TextView(this).apply { text = "⌖ ${person.distance}"; textSize = 10f; setTextColor(muted) }
            card.addView(dist)
            val lp = GridLayout.LayoutParams().apply {
                width = 0; height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(dp(2), dp(2), dp(2), dp(3))
            }
            grid.addView(card, lp)
        }
        content.addView(grid)
        val note = TextView(this).apply {
            text = "Demo profiles shown • Connect your backend to display real members and photos."
            textSize = 11f; setTextColor(muted); setPadding(0, dp(10), 0, dp(12))
        }
        content.addView(note)
    }

    private fun profilePreview(p: Person) {
        content.removeAllViews()
        title("${p.name}, ${p.age}", "⌖ ${p.distance} away  •  ${p.tag}")
        val hero = TextView(this).apply {
            text = p.name.take(1); textSize = 88f; gravity = Gravity.CENTER; setTextColor(Color.WHITE)
            background = shape(p.color, 24)
        }
        content.addView(hero, matchWrap(220))
        title("About", "Here to meet good people, make connections and enjoy life.")
        action("♡  Send like", pink) { toast("Likes will work after account and backend integration.") }
        action("Message", purple) { show("Chat") }
        action("Upgrade for more", panel) { show("Premium") }
    }

    private fun premiumPage() {
        title("Unlock more with BONDINGS", "Choose how you want to connect.")
        plan("FREE", "₹0", "Profile discovery\nBasic filters\nLimited likes\nBasic messaging", "Current plan", false)
        plan("BONDINGS PLUS", "₹199 / month", "Everything in Free\nUnlimited likes\nAdvanced filters\nAd-free experience", "Choose Plus", true)
        plan("BONDINGS PREMIUM", "₹399 / month", "Everything in Plus\nSee who liked you\nIncognito discovery\nProfile boosts\nPriority discovery", "Choose Premium", true)
        val info = TextView(this).apply {
            text = "Subscription checkout is not active yet. Google Play Billing and server-side verification must be added before accepting payments."
            textSize = 11f; setTextColor(muted); setPadding(dp(4), dp(12), dp(4), dp(20))
        }
        content.addView(info)
    }

    private fun plan(name: String, price: String, features: String, button: String, paid: Boolean) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(17), dp(18), dp(17))
            background = shape(if(paid) Color.rgb(39, 24, 61) else panel, 20)
        }
        val n = TextView(this).apply { text = name; textSize = 12f; letterSpacing = .12f; typeface = Typeface.DEFAULT_BOLD; setTextColor(if(name.contains("PREMIUM")) pink else Color.WHITE) }
        box.addView(n)
        val pr = TextView(this).apply { text = price; textSize = 26f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.WHITE); setPadding(0, dp(5), 0, dp(10)) }
        box.addView(pr)
        val ft = TextView(this).apply { text = features; textSize = 13f; setTextColor(muted); setLineSpacing(dp(4).toFloat(), 1f) }
        box.addView(ft)
        val btn = TextView(this).apply {
            text = button; gravity = Gravity.CENTER; textSize = 13f; typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE); setPadding(dp(12), dp(13), dp(12), dp(13))
            background = shape(if(paid) pink else Color.rgb(66, 52, 83), 14)
            setOnClickListener { toast(if(paid) "Plan selected. Payment activation is the next development step." else "You are viewing the Free plan.") }
        }
        box.addView(btn, matchWrap(44))
        content.addView(box, matchWrap(0).apply { bottomMargin = dp(13) })
    }

    private fun profilePage() {
        title("Your profile", "Make your profile stand out.")
        val avatar = TextView(this).apply {
            text = "＋\nAdd your photos"; gravity = Gravity.CENTER; textSize = 16f; setTextColor(Color.WHITE)
            background = shape(panel, 22)
        }
        content.addView(avatar, matchWrap(150))
        title("Complete your profile", "Add your name, age, bio, interests and photos to help people get to know you.")
        action("Edit profile", purple) { toast("Profile editor will be connected to account storage.") }
        action("Privacy & safety", panel) { toast("Block, report and privacy settings are planned for the backend integration.") }
        action("View membership plans", pink) { show("Premium") }
    }

    private fun simplePage(heading: String, body: String, button: String) {
        title(heading, body)
        val spacer = View(this)
        content.addView(spacer, LinearLayout.LayoutParams(1, dp(45)))
        action(button, purple) { show("Discover") }
    }

    private fun title(head: String, sub: String) {
        val h = TextView(this).apply { text = head; textSize = 23f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.WHITE) }
        content.addView(h)
        val s = TextView(this).apply { text = sub; textSize = 12f; setTextColor(muted); setPadding(0, dp(5), 0, dp(16)) }
        content.addView(s)
    }

    private fun action(label: String, color: Int, click: () -> Unit) {
        val b = TextView(this).apply {
            text = label; gravity = Gravity.CENTER; textSize = 14f; typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE); setPadding(dp(14), dp(14), dp(14), dp(14))
            background = shape(color, 14); setOnClickListener { click() }
        }
        content.addView(b, matchWrap(50).apply { bottomMargin = dp(10) })
    }

    private fun shape(color: Int, radius: Int): GradientDrawable =
        GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat() }

    private fun matchWrap(height: Int): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(-1, if(height == 0) -2 else dp(height))

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}
