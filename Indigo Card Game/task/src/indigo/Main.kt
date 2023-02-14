package indigo

import kotlin.system.exitProcess

enum class Suits(val symbol: String) {
    SPADES("♠"),
    HEARD("♥"),
    DIAMONDS("♦"),
    CLUBS("♣")
}

enum class Ranks(val symbol: String) {
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
    KING("K")
}

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}

enum class Deck(var cards: MutableList<Card>) {
    GAME_DECK(mutableListOf()),
    TABLE_DECK(mutableListOf())
}

enum class Players(var value: Player) {
    PLAYER(Player("Player")),
    COMPUTER(Player("Computer"))
}

class Player(val name: String) {
    var firstTurn: Boolean = false
    var turn: Boolean = false
    var lastWinner: Boolean = false
    var deck: MutableList<Card> = mutableListOf()
    var score: Int = 0
    var winsCards: MutableList<Card> = mutableListOf()
}

fun main() {
    // Start the game0
    startGame()

    // Main game
    game()
}

private fun startGame() {
    println("Indigo Card Game")

//    Ask questions until we find out who goes first
    while (!Players.PLAYER.value.turn && !Players.COMPUTER.value.turn) {
        println("Play first?")
        when (readln().lowercase()) {
            "yes" -> {
                Players.PLAYER.value.firstTurn = true
                Players.PLAYER.value.turn = true
                Players.PLAYER.value.lastWinner = true
            }
            "no" -> {
                Players.COMPUTER.value.firstTurn = true
                Players.COMPUTER.value.turn = true
                Players.COMPUTER.value.lastWinner = true
            }
        }
    }

//    Let's get(create) a deck and deal cards on table
    Suits.values().forEach { suit ->
        Ranks.values().forEach { rank ->
            Deck.GAME_DECK.cards.add(Card(rank, suit))
        }
    }
    Deck.GAME_DECK.cards.shuffle()

    // RULES - Four cards are placed face-up on the table
    Deck.TABLE_DECK.cards = Deck.GAME_DECK.cards.subList(0, 4)
    println("Initial cards on the table: ${Deck.TABLE_DECK.cards.joinToString(" ")}")

    // TODO() FIX crutch for deleting a value  -  fix this!
    val temp = Deck.GAME_DECK.cards.filter { it !in Deck.TABLE_DECK.cards }.toMutableList()
    Deck.GAME_DECK.cards = temp

    // RULES - Six cards are dealt to each player;
    dealCards()
}

private fun game() {

//    The players take turns in playing cards.
    while (Players.PLAYER.value.turn || Players.COMPUTER.value.turn) {

        if (Players.PLAYER.value.deck.size + Players.COMPUTER.value.deck.size == 52) gameOver()

        // RULES - When both players have no cards in hand, deal cards.
        if (Players.PLAYER.value.deck.isEmpty() && Players.COMPUTER.value.deck.isEmpty()) dealCards()

        messageText()

        when {
            Players.PLAYER.value.turn -> {
                val cards =
                    Players.PLAYER.value.deck
                        .mapIndexed { index, card -> "${index + 1})$card" }
                        .joinToString(" ")
                println("Cards in hand: $cards")
                move(Players.PLAYER)
            }
            Players.COMPUTER.value.turn -> move(Players.COMPUTER)
        }

        if (
            Deck.GAME_DECK.cards.isEmpty() &&
            Players.PLAYER.value.deck.isEmpty() &&
            Players.COMPUTER.value.deck.isEmpty()
        ) {
            messageText()
            gameOver()
        }
    }
}


private fun dealCards() {
    // Take turns dealing out cards
    for (index in 0..11) {
        if (index % 2 == 0) {
            Players.COMPUTER.value.deck.add(Deck.GAME_DECK.cards[index])
        } else {
            Players.PLAYER.value.deck.add(Deck.GAME_DECK.cards[index])
        }
    }

    // Remove cards from deck
    Deck.GAME_DECK.cards.removeAll(Players.COMPUTER.value.deck + Players.PLAYER.value.deck)
}

private fun move(currentPlayer: Players) {
    val currentPlayerValues = currentPlayer.value
    val playerValue = Players.PLAYER.value
    val computerValue = Players.COMPUTER.value

    val tableCards = Deck.TABLE_DECK.cards

    lateinit var playingCard: Card

    fun putCard() {

        currentPlayerValues.deck.remove(playingCard)

        // RULES - If the card has the same suit or rank as the topmost card, then the player wins all the cards on the table;
        if (tableCards.isNotEmpty()) {
            if (playingCard.rank == tableCards.last().rank || playingCard.suit == tableCards.last().suit) {

                tableCards.add(tableCards.size, playingCard)
                currentPlayerValues.winsCards.addAll(tableCards)
                tableCards.clear()

                println("${currentPlayerValues.name} wins cards")

                currentPlayerValues.score = currentPlayerValues.winsCards.filter {
                    it.rank in listOf(
                        Ranks.ACE,
                        Ranks.TEN,
                        Ranks.JACK,
                        Ranks.QUEEN,
                        Ranks.KING
                    )
                }.size

                statistic()

                playerValue.lastWinner = false
                computerValue.lastWinner = false
                currentPlayerValues.lastWinner = true

            } else {
                tableCards.add(playingCard)
            }
        } else {
            tableCards.add(playingCard)
        }

        // Change players turn
        playerValue.turn = true
        computerValue.turn = true
        currentPlayerValues.turn = false
    }

    when (currentPlayer) {
        // when user move
        Players.PLAYER -> {

            // output cards in hand user
            println("Choose a card to play (1-${currentPlayerValues.deck.size}):")

            // received player choice
            when (val choice = readln()) {
                in (1..currentPlayerValues.deck.size).map { it.toString() } -> {
                    playingCard = currentPlayerValues.deck[choice.toInt() - 1]
                    putCard()
                }
                "exit" -> {
                    println("Game Over")
                    exitProcess(0)
                }
                else -> move(currentPlayer)
            }
        }
        // when computer move
        Players.COMPUTER -> {
//            playingCard = currentPlayerValues.deck.first() // TODO REMOVE THIS
            val candidateCards: List<Card>
            var cardsSameSuits = listOf<Card>()
            var cardsSameRanks = listOf<Card>()

            if (currentPlayerValues.deck.filter { card ->
                    card.rank == tableCards.last().rank ||
                            card.suit == tableCards.last().suit
                }.isEmpty()
            ) {
                candidateCards = currentPlayerValues.deck
            } else {
                candidateCards = currentPlayerValues.deck.filter { card ->
                    card.rank != tableCards.last().rank ||
                            card.suit != tableCards.last().suit
                }

                if (candidateCards.groupBy { it.suit }.filter { it.value.size > 1 }.values.reduce { acc, cards -> acc + cards }.isNotEmpty())
                    cardsSameSuits = candidateCards.groupBy { it.suit }.filter { it.value.size > 1 }.values.reduce { acc, cards -> acc + cards }

                if (candidateCards.groupBy { it.rank }.filter { it.value.size > 1 }.values.reduce { acc, cards -> acc + cards }.isNotEmpty())
                    cardsSameRanks = candidateCards.groupBy { it.rank }.filter { it.value.size > 1 }.values.reduce { acc, cards -> acc + cards }
            }

            // for steps 3 and 4
            playingCard = when {
                // 3) If there are no cards on the table
                // 4) If there are cards on the table but no candidate cards, use the same tactics as in step 3
                tableCards.isEmpty() -> when {
                    // If there are cards in hand with the same suit, throw one of them at random
                    cardsSameSuits.isNotEmpty() -> cardsSameSuits.random()
                    // If there are no cards in hand with the same suit, but there are cards with the same rank, then throw one of them at random
                    cardsSameRanks.isNotEmpty() -> cardsSameRanks.random()
                    // If there are no cards in hand with the same suit or rank, throw any card at random.
                    else -> currentPlayerValues.deck.random()
                }

                // 1) If there is only one card in hand, put it on the table
                currentPlayerValues.deck.size == 1 -> currentPlayerValues.deck.first()

                // 2) If there is only one candidate card, put it on the table
                candidateCards.size == 1 -> candidateCards.first()

                // 5) If there are two or more candidate cards
                else -> {
                    when {
                        // If there are 2 or more candidate cards with the same suit as the top card on the table, throw one of them at random
                        cardsSameSuits.size >= 2 -> cardsSameSuits.random()
                        // If the above isn't applicable, but there are 2 or more candidate cards with the same rank as the top card on the table, throw one of them at random
                        cardsSameRanks.size >= 2 -> cardsSameRanks.random()
                        // If nothing of the above is applicable, then throw any of the candidate cards at random.
                        else -> candidateCards.random()
                    }
                }
            }

            println("Computer plays $playingCard")
            putCard()
        }
    }
}

fun gameOver() {
    val player = Players.PLAYER.value
    val computer = Players.COMPUTER.value
    val lastWinner = if (player.lastWinner) player else computer
    lastWinner.winsCards.addAll(Deck.TABLE_DECK.cards)

    fun calcScore(player: Player) : Int {
        return player.winsCards.filter {
            it.rank in listOf(
                Ranks.ACE,
                Ranks.TEN,
                Ranks.JACK,
                Ranks.QUEEN,
                Ranks.KING
            )
        }.size
    }
    player.score = calcScore(player)
    computer.score = calcScore(computer)

    if (player.score > computer.score) player.score += 3
    if (player.score < computer.score) computer.score += 3

    statistic()

    println("Game Over")
    exitProcess(0)
}

fun statistic() {
    val player = Players.PLAYER.value
    val computer = Players.COMPUTER.value

    println(
        """
           Score: ${player.name} ${player.score} - ${computer.name} ${computer.score}
           Cards: ${player.name} ${player.winsCards.size} - ${computer.name} ${computer.winsCards.size}
        """.trimIndent()
    )
}

fun messageText() = println(
    if (Deck.TABLE_DECK.cards.isEmpty())
        "\nNo cards on the table"
    else
        "\n${Deck.TABLE_DECK.cards.size} cards on the table, and the top card is ${Deck.TABLE_DECK.cards.last()}"
)

