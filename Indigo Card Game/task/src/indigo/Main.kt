package indigo

enum class Suits (val symbol: String) {
    SPADES("♠"),
    HEARD("♥"),
    DIAMONDS("♦"),
    CLUBS("♣");
    companion object {
        override fun toString() = values().joinToString(" ") { it.symbol }
    }
}

enum class Ranks (val symbol: String) {
    ACE("A"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K");

    companion object {
        override fun toString() = values().joinToString(" ") { it.symbol }
    }
}
class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}

fun main() {

    chooseAnAction()

}

fun chooseAnAction() {
    println("Choose an action (reset, shuffle, get, exit):")
    when (readln()) {
        "reset" -> reset()
        "shuffle" -> shuffle()
        "get" -> get()
        "exit" -> bye()
        else -> wrongAction()
    }
}

fun wrongAction() {
    println("Wrong action.")
    chooseAnAction()
}

fun bye() {
    println("Bye")
}

fun reset() {
    val deck = mutableListOf<Card>()
    Ranks.values().forEach {rank ->
        Suits.values().forEach { suit ->
            deck.add(Card(rank, suit))
        }
    }
    // Test deck
//    deck.forEach { print("$it ") }
    println("Card deck is reset.\n")


    chooseAnAction()
}

fun shuffle() {
    TODO("Change the order of the remaining cards in the deck;\n" +
            "Print the message Card deck is shuffled.;\n" +
            "Prompt for new action.")
    chooseAnAction()
}

fun get() {
    TODO("Ask for the number of cards to be taken from the top of the deck with the prompt Number of cards:\n" +
            "Users should input a number between 1 and 52. Otherwise, for any number outside this range or for any non-number input, print Invalid number of cards. (Example 2);\n" +
            "These cards are to be removed from the top of the deck and printed divided by one space (6♦ 3♦ 8♦ 4♠ 9♦). If the number of cards is larger than the number of the remaining cards in the deck, print The remaining cards are insufficient to meet the request. (Example 3);\n" +
            "Prompt for new action.")
    chooseAnAction()
}
