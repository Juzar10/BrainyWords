package com.example.brainywords.data

data class Word(
    val word: String,
    val definition: String,
    val synonyms: List<String>,
    val quote: String,
    val author: String,
    val source: String
)

object WordData {
    val words = listOf(
        Word(
            word = "Incorrigible",
            definition = "(adj). Impossible to correct, reform, or improve; persistently bad in behavior or habits.",
            synonyms = listOf("Unreformable", "Irredeemable", "Incurable", "Hopeless", "Intractable"),
            quote = "Lestrade is an excellent fellow and as incorrigible as a ruffian as he is in his police methods, he has a certain dogged tenacity which I admire",
            author = "Sherlock Holmes",
            source = "The Adventure of the Six Napoleons"
        ),
        Word(
            word = "Ephemeral",
            definition = "(adj). Lasting for a very short time; transitory.",
            synonyms = listOf("Fleeting", "Transient", "Momentary", "Brief", "Short-lived"),
            quote = "Beauty is ephemeral, but its memory is eternal",
            author = "Anonymous",
            source = "Classical Literature"
        ),
        Word(
            word = "Ubiquitous",
            definition = "(adj). Present, appearing, or found everywhere.",
            synonyms = listOf("Omnipresent", "Pervasive", "Universal", "Widespread", "Ever-present"),
            quote = "In the digital age, smartphones have become ubiquitous companions",
            author = "Tech Observer",
            source = "Modern Technology Review"
        ),
        Word(
            word = "Serendipity",
            definition = "(n). The occurrence and development of events by chance in a happy or beneficial way.",
            synonyms = listOf("Fortune", "Luck", "Chance", "Coincidence", "Fluke"),
            quote = "The greatest discoveries often come from serendipity rather than systematic searching",
            author = "Marie Curie",
            source = "Scientific Memoirs"
        ),
        Word(
            word = "Mellifluous",
            definition = "(adj). Having a smooth, flowing, honey-like sound.",
            synonyms = listOf("Melodious", "Musical", "Harmonious", "Sweet-sounding", "Euphonious"),
            quote = "Her mellifluous voice could calm even the most troubled soul",
            author = "Literary Critic",
            source = "Poetry Analysis"
        ),
        Word(
            word = "Eloquent",
            definition = "(adj). Fluent or persuasive in speaking or writing.",
            synonyms = listOf("Articulate", "Expressive", "Persuasive", "Fluent", "Well-spoken"),
            quote = "An eloquent speaker can turn the simplest words into profound wisdom",
            author = "Aristotle",
            source = "Rhetoric"
        ),
        Word(
            word = "Tenacious",
            definition = "(adj). Tending to keep a firm hold of something; clinging or adhering closely.",
            synonyms = listOf("Persistent", "Determined", "Resolute", "Stubborn", "Unwavering"),
            quote = "Success belongs to the most tenacious",
            author = "Napoleon Bonaparte",
            source = "Military Maxims"
        ),
        Word(
            word = "Perspicacious",
            definition = "(adj). Having a ready insight into and understanding of things.",
            synonyms = listOf("Perceptive", "Astute", "Sharp", "Discerning", "Insightful"),
            quote = "A perspicacious mind can see through the veils of deception",
            author = "Detective Novel",
            source = "Mystery Literature"
        )
    )
}