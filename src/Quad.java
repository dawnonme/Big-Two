
/**
 * The class for hand Full House in Big Two game. Subclass of Hand.
 * 
 * @author Li Gengyu
 *
 */
public class Quad extends Hand {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Quad class, simply call the constructor of the base Hand class.
	 * 
	 * @param player
	 * 				Player who play this hand
	 * 
	 * @param cards
	 * 				List of cards in this hand.
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
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
		if (getCard(0).getRank() == getCard(1).getRank()) {
			for (int i = 0; i < 4; i++) {
				if (getCard(i).getSuit() == 3) {
					return getCard(i);
				}
			}
		} else if (getCard(3).getRank() == getCard(4).getRank()) {
			for (int i = 1; i < 5; i++) {
				if (getCard(i).getSuit() == 3) {
					return getCard(i);
				}
			}
		}
		return null;
	}

	/**
	 * Method to judge whether this hand beats that hand. Override because Quad > FullHouse > Flush > Straight
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
		if (hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse") {
			return true;
		} else if (hand.getType() == "Quad") {
			if (getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Judge the Quad hand is valid or not.
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
			if (getCard(0).getRank() == getCard(1).getRank()) {
				if (getCard(1).getRank() == getCard(2).getRank() && getCard(2).getRank() == getCard(3).getRank()) {
					return true;
				}
			} else if (getCard(3).getRank() == getCard(4).getRank()) {
				if (getCard(3).getRank() == getCard(2).getRank() && getCard(2).getRank() == getCard(1).getRank()) {
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
		return "Quad";
	}
	
	
}
