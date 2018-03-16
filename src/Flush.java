
/**
 * The class for hand Flush in Big Two game. Subclass of Hand.
 * 
 * @author Li Gengyu
 *
 */
public class Flush extends Hand {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Single class, simply call the constructor of the base Hand class.
	 * 
	 * @param player
	 * 				Player who play this hand
	 * 
	 * @param cards
	 * 				List of cards in this hand.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * Judge the Flush hand is valid or not.
	 * 
	 * @return 
	 * 			A boolean value true if valid, and vice versa.
	 * 
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		if (size() == 5) {
			for (int i = 0; i < 4; i++) {
				if (getCard(i).getSuit() != getCard(i + 1).getSuit()) {
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
		return "Flush";
	}

	/**
	 * Method to judge whether this hand beats that hand. Override because Flush > Straight
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
		if (hand.getType() == "Straight") {
			return true;
		} else if (hand.getType() == "Flush") {
			if (getCard(0).getSuit() > hand.getCard(0).getSuit()) {
				return true;
			} else if (getCard(0).getSuit() > hand.getCard(0).getSuit()) {
				return false;
			} else {
				return super.beats(hand);
			}
		}
		return false;
	}
}
