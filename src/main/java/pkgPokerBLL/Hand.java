package pkgPokerBLL;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.sun.javafx.scene.control.skin.VirtualFlow.ArrayLinkedList;

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
	
	private ArrayList<Hand> replaceJoker(Hand h, int location)
	{
		ArrayList<Hand> replacmentHands=new ArrayList<Hand>();
		
		for (eRank Rank : eRank.values()) {
			
			for(eSuit Suit: eSuit.values())
			{
				Hand tempHand=h;
				tempHand.AddCardToHand(new Card(Rank,Suit));
				tempHand.getCardsInHand().remove(location);
				replacmentHands.add(tempHand);
			}
			
		}
		
		return replacmentHands;
		
	}

	public Hand EvaluateHand() throws HandException {
//		
		ArrayList<Hand> uncheckedHands=new ArrayList<Hand>();
		ArrayList<Hand> checkedHands=new ArrayList<Hand>();
		
		uncheckedHands.add(this);
		
		
		
		while(uncheckedHands.size()!=0)
		{
			
			Hand tempHand=uncheckedHands.get(0);
			boolean hasJoker=false;
			int jokerLocation=-1;
			
			
			//check every crd in the hand to see if its a joker
			for(int i=0; i<tempHand.getCardsInHand().size();i++)
			{				
				Card c=tempHand.getCardsInHand().get(i);
				if(c.geteRank()==eRank.JOKER||c.isWild())
				{
					hasJoker=true;
					jokerLocation=i;
					break;
				}
			}
			
			//if there is a joker fill in all the possible hands that could come from it
			if(hasJoker)
			{
				uncheckedHands.addAll(replaceJoker(tempHand, jokerLocation));
			}
			else
			{
				checkedHands.add(tempHand);
			}
			
			//no matter what remove the current hand since if it is not a joker it is moved to the other list 
			// and dosent need to be checked. if it did have a joker al the possiblities have been filled in
			uncheckedHands.remove(0);
			
		}
		
		Hand highestHand=checkedHands.get(0);
		
		for(Hand h: checkedHands)
		{
			EvaluateHand(h);
			EvaluateHand(highestHand);
			
			
			if(h.HS.getHandStrength().ordinal()<=highestHand.HS.getHandStrength().ordinal())
			{
				highestHand=h;
			}
		}
		
		
		return highestHand;
		
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
		boolean isRoyalFlush=false;
		
		if( h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE
			&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.KING
			&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == eRank.QUEEN
			&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == eRank.JACK
			&& h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank() == eRank.TEN
				)		{
			isRoyalFlush=true;
			hs.setHandStrength(eHandStrength.RoyalFlush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
			
		}
		
		return isRoyalFlush;
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

	public static boolean isStraight(ArrayList<Card> cards, Card c) {
		boolean isStraight = false;
		int iStartCard = (Hand.isAce(cards)) ? eCardNo.SecondCard.getCardNo() : eCardNo.FirstCard.getCardNo();

		for (; iStartCard < 4; iStartCard++) {
			if (cards.get(iStartCard).geteRank().getiRankNbr() - 1 == cards.get(iStartCard + 1).geteRank()
					.getiRankNbr()) {
				isStraight = true;
			} else {
				isStraight = false;
				break;
			}
		}

		if (isStraight) {
			if (cards.get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE) {
				if (cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.KING) {
					c.seteRank(cards.get(eCardNo.FirstCard.getCardNo()).geteRank());
					c.seteSuit(cards.get(eCardNo.FirstCard.getCardNo()).geteSuit());
				} else if (cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.FIVE) {
					c.seteRank(cards.get(eCardNo.SecondCard.getCardNo()).geteRank());
					c.seteSuit(cards.get(eCardNo.SecondCard.getCardNo()).geteSuit());

				}
			} else {
				c.seteRank(cards.get(eCardNo.FirstCard.getCardNo()).geteRank());
				c.seteSuit(cards.get(eCardNo.FirstCard.getCardNo()).geteSuit());
			}
		}

		return isStraight;
	}

	public static boolean isAce(ArrayList<Card> cards) {
		if ((cards.get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE)
				&& (cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.KING)
				|| (cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.FIVE)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isHandStraight(Hand h, HandScore hs) {

		boolean bIsStraight = false;
		Card c = new Card();

		if (isStraight(h.getCardsInHand(), c)) {
			hs.setHandStrength(eHandStrength.Straight);
			hs.setHiHand(c.geteRank());
			hs.setLoHand(null);
			bIsStraight = true;
		}
		return bIsStraight;
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

		boolean isHandTwoPair = false;

		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank())) {
			isHandTwoPair = true;
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank());
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));

		} else if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isHandTwoPair = true;
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
			kickers.add(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));

		} else if ((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isHandTwoPair = true;
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));

		}

		if (isHandTwoPair) {
			hs.setHandStrength(eHandStrength.TwoPair);
			hs.setKickers(kickers);
		}
		return isHandTwoPair;

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
		
		boolean isHighCard=false;
		
		if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank()) != h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() != h.getCardsInHand()
						.get(eCardNo.ThirdCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() != h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() != h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			isHighCard=true;
			
			hs.setHandStrength(eHandStrength.HighCard);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		}
		
		return isHighCard;
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
