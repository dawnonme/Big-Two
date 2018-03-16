
/**
 * The class for hand Straight in Big Two game. Subclass of Hand.
 * 
 * @author Li Gengyu
 *
 */
public class Straight extends Hand {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Straight class, simply call the constructor of the base Hand class.
	 * 
	 * @param player
	 * 				Player who play this hand
	 * 
	 * @param cards
	 * 				List of cards in this hand.
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * Judge the Straight hand is valid or not.
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
		return "Straight";
	}
}
