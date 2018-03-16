
/**
 * The class for hand Straight Flush in Big Two game. Subclass of Hand.
 * 
 * @author Li Gengyu
 *
 */
public class StraightFlush extends Hand {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of StraightFlush class, simply call the constructor of the base Hand class.
	 * 
	 * @param player
	 * 				Player who play this hand
	 * 
	 * @param cards
	 * 				List of cards in this hand.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
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
		if (hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse" || hand.getType() == "Quad") {
			return true;
		} else if (hand.getType() == "StraightFlush") {
			if (getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Judge the StraightFlush hand is valid or not.
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
			for (int i = 0; i < 4; i++) {
				if (getCard(i).getSuit() != getCard(i + 1).getSuit()) {
					return false;
				}
				if ((getCard(i).getRank() + 1) % 13 != getCard(i + 1).getRank()) {
					return false;
				}
			}
			return true;
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
		return "StraightFlush";
	}
}
