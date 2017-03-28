package pkgPokerBLL;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import PkgException.HandException;
import pkgPokerEnum.eCardNo;
import pkgPokerEnum.eHandStrength;
import pkgPokerEnum.eRank;
import pkgPokerEnum.eSuit;

public class Hand {

	private UUID HandID;
	private boolean bIsScored;
	private HandScore HS;
	private ArrayList<Card> CardsInHand = new ArrayList<Card>();

	public Hand() {

	}

	public void AddCardToHand(Card c) {
		CardsInHand.add(c);
	}

	public ArrayList<Card> getCardsInHand() {
		return CardsInHand;
	}

	public HandScore getHandScore() {
		return HS;
	}

	public Hand EvaluateHand() throws HandException {
		Hand h = Hand.EvaluateHand(this);
		return h;
	}

	private static Hand EvaluateHand(Hand h) throws HandException {
		
		if(h.getCardsInHand().size()!=5)
		{
			throw new HandException(h);
		}
		
		Collections.sort(h.getCardsInHand());

		// Another way to sort
		// Collections.sort(h.getCardsInHand(), Card.CardRank);

		HandScore hs = new HandScore();
		try {
			Class<?> c = Class.forName("pkgPokerBLL.Hand");

			for (eHandStrength hstr : eHandStrength.values()) {
				Class[] cArg = new Class[2];
				cArg[0] = pkgPokerBLL.Hand.class;
				cArg[1] = pkgPokerBLL.HandScore.class;

				Method meth = c.getMethod(hstr.getEvalMethod(), cArg);
				Object o = meth.invoke(null, new Object[] { h, hs });

				// If o = true, that means the hand evaluated- skip the rest of
				// the evaluations
				if ((Boolean) o) {
					break;
				}
			}

			h.bIsScored = true;
			h.HS = hs;

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return h;
	}

	// TODO: Implement This Method
	public static boolean isHandRoyalFlush(Hand h, HandScore hs) {
		return false;
	}

	// TODO: Implement This Method
	public static boolean isHandStraightFlush(Hand h, HandScore hs) {
		return false;
	}
	//TODO: Finish this Method
	public static boolean isHandFiveOfAKind(Hand h, HandScore hs)
	{
		boolean isFiveKind=false;
		
		if( h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()
			&& h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.ThirdCard.getCardNo()).geteRank()
			&& h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FourthCard.getCardNo()).geteRank()
			&& h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FifthCard.getCardNo()).geteRank())
		{
			isFiveKind=true;
		}
		hs.setHandStrength(eHandStrength.FiveOfAKind);
		hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
		hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		return isFiveKind;
	}
	
	public static boolean isHandFourOfAKind(Hand h, HandScore hs) {
		boolean isFourKind = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.ThirdCard.getCardNo()).geteRank()
						&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
			isFourKind = true;
			hs.setHandStrength(eHandStrength.FourOfAKind);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} else if((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank()
						&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			isFourKind = true;
			hs.setHandStrength(eHandStrength.FourOfAKind);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		}
		
		return isFourKind;
	}

	public static boolean isHandFlush(Hand h, HandScore hs) {
		boolean isFlush = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit()) == eSuit.CLUBS
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteSuit() == eSuit.CLUBS
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteSuit() == eSuit.CLUBS
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteSuit() == eSuit.CLUBS
				&& h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteSuit() == eSuit.CLUBS) {
			isFlush = true;
			hs.setHandStrength(eHandStrength.Flush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());	
		} else if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit()) == eSuit.DIAMONDS
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteSuit() == eSuit.DIAMONDS
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteSuit() == eSuit.DIAMONDS
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteSuit() == eSuit.DIAMONDS
				&& h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteSuit() == eSuit.DIAMONDS) {
			isFlush = true;
			hs.setHandStrength(eHandStrength.Flush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} else if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit()) == eSuit.SPADES
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteSuit() == eSuit.SPADES
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteSuit() == eSuit.SPADES
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteSuit() == eSuit.SPADES
				&& h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteSuit() == eSuit.SPADES) {
			isFlush = true;
			hs.setHandStrength(eHandStrength.Flush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} else if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit()) == eSuit.HEARTS
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteSuit() == eSuit.HEARTS
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteSuit() == eSuit.HEARTS
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteSuit() == eSuit.HEARTS
				&& h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteSuit() == eSuit.HEARTS) {
			isFlush = true;
			hs.setHandStrength(eHandStrength.Flush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		}
		
		return isFlush;
	}

	// TODO: Implement This Method
	public static boolean isHandStraight(Hand h, HandScore hs) {
		return false;
	}

	public static boolean isHandThreeOfAKind(Hand h, HandScore hs) {
		boolean isThreeKind = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.ThirdCard.getCardNo()).geteRank()) {
			isThreeKind = true;
			hs.setHandStrength(eHandStrength.ThreeOfAKind);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} else if((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
			isThreeKind = true;
			hs.setHandStrength(eHandStrength.ThreeOfAKind);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} else if((h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			isThreeKind = true;
			hs.setHandStrength(eHandStrength.ThreeOfAKind);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		}
		
		return isThreeKind;
	}

	public static boolean isHandTwoPair(Hand h, HandScore hs) {
		boolean isTwoPair = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
			isTwoPair = true;
			hs.setHandStrength(eHandStrength.TwoPair);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} else if ((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			isTwoPair = true;
			hs.setHandStrength(eHandStrength.TwoPair);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} 
		
		return isTwoPair;
	}

	public static boolean isHandPair(Hand h, HandScore hs) {
		boolean isPair = false;

		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank()) == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()
				|| h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.ThirdCard.getCardNo()).geteRank()
				|| h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank()
				|| h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			isPair = true;
			hs.setHandStrength(eHandStrength.Pair);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} 

		return isPair;
	}

	// TODO: Implement This Method
	public static boolean isHandHighCard(Hand h, HandScore hs) {
		return false;
	}

	public static boolean isHandAcesAndEights(Hand h, HandScore hs) {
		boolean AcesAndEights = false;

		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == eRank.ACE
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == eRank.EIGHT
				&& h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank() == eRank.EIGHT)) {
			AcesAndEights = true;
			hs.setHandStrength(eHandStrength.AcesAndEights);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
		} 

		return AcesAndEights;
	}

	public static boolean isHandFullHouse(Hand h, HandScore hs) {

		boolean isFullHouse = false;

		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isFullHouse = true;
			hs.setHandStrength(eHandStrength.FullHouse);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
		} else if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isFullHouse = true;
			hs.setHandStrength(eHandStrength.FullHouse);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
		}

		return isFullHouse;

	}
}
