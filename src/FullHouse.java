
/**
 * The class for hand Full House in Big Two game. Subclass of Hand.
 * 
 * @author Li Gengyu
 *
 */
public class FullHouse extends Hand {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of FullHouse class, simply call the constructor of the base Hand class.
	 * 
	 * @param player
	 * 				Player who play this hand
	 * 
	 * @param cards
	 * 				List of cards in this hand.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * Method to judge whether this hand beats that hand. Override because FullHouse > Flush > Straight
	 * 
	 * @param hand
	 * 				Another hand for comparing.
	 * 
	 * @return
	 * 			A boolean value to indicate whether this hand beats that hand or not, true if beats,
	 *          and vice versa.
	 */
	@Override
	public boolean beats(Hand hand) {
		if (hand.getType() == "Straight" || hand.getType() == "Flush") {
			return true;
		} else if (hand.getType() == "FullHouse") {
			if (getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Judge the FullHouse hand is valid or not.
	 * 
	 * @return 
	 * 			A boolean value true if valid, and vice versa.
	 * 
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		if (size() == 5) {
			sort();
			if (getCard(0).getRank() == getCard(1).getRank() && getCard(0).getRank() == getCard(2).getRank()) {
				if (getCard(3).getRank() == getCard(4).getRank()) {
					return true;
				}
			} else if (getCard(2).getRank() == getCard(3).getRank() && getCard(2).getRank() == getCard(4).getRank()) {
				if (getCard(0).getRank() == getCard(1).getRank()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Retrieve which type of hand it is.
	 * 
	 * @return
	 * 			A String object of the class's name,
	 * 
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "FullHouse";
	}

	/**
	 * Method to retrieve the top card according to the compareTo method, of this hand, usually used to compare
	 * to same type hands.
	 * 
	 * @return
	 * 			Return the top card in the hand. Or in the card list.
	 */
	@Override
	public Card getTopCard() {
		sort();
		if (getCard(0).getRank() == getCard(1).getRank() && getCard(0).getRank() == getCard(2).getRank()) {
			int index = 0, suit = getCard(0).getSuit();
			for (int i = 1; i < 3; i++) {
				if (getCard(i).getSuit() > suit) {
					index = i;
					suit = getCard(i).getSuit();
				}
			}
			return getCard(index);
		} else if (getCard(2).getRank() == getCard(3).getRank() && getCard(2).getRank() == getCard(4).getRank()) {
			int index = 0, suit = getCard(2).getSuit();
			for (int i = 3; i < 5; i++) {
				if (getCard(i).getSuit() > suit) {
					index = i;
					suit = getCard(i).getSuit();
				}
			}
			return getCard(index);
		} else {
			return null;
		}
	}
}
