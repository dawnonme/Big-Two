
/**
 * This class models the card used in Big Two game, which is the subclass of Card class. This class override the
 * compareTo method for A and 2 are actually not in there normal position.
 * 
 * @author Li Gengyu
 *
 */
public class BigTwoCard extends Card {

	/**
	 * This is a default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of BigTwoCard class, which simply use the super class's constructor.
	 * 
	 * @param suit
	 * 				The suit of the BigTwoCard 0, 1, 2, 3
	 * 
	 * @param rank
	 * 				The rank of the BigTwoCard 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A, 2
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * This method is an override method of compareTo method in Card class, which is designed to compare two 
	 * BigTwoCard objects. Rank have the first priority, if ranks equal, higher suit means larger. Because the
	 * abnormal position of A and 2, this function should be overridden.
	 * 
	 * @param card
	 * 				Another card which is about to be compared.
	 * 
	 * @return
	 * 			Return 1 if this is larger than card, 0 if the same, -1 if smaller. 
	 * 
	 * @see Card#compareTo(Card)
	 */
	@Override
	public int compareTo(Card card) {
		int rankOfThis = getRank(), rankOfCard = card.getRank();
		if (rankOfThis == 0 || rankOfThis == 1) {
			rankOfThis += 13;
		}
		if (rankOfCard == 0 || rankOfCard == 1) {
			rankOfCard += 13;
		}
		if (rankOfThis > rankOfCard) {
			return 1;
		} else if (rankOfThis < rankOfCard) {
			return -1;
		} else if (getSuit() > card.getSuit()) {
			return 1;
		} else if (getSuit() < card.getSuit()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
