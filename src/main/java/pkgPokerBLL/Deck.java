package pkgPokerBLL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import PkgException.DeckException;
import pkgPokerEnum.eRank;
import pkgPokerEnum.eSuit;

public class Deck {

	private UUID DeckID;
	private ArrayList<Card> DeckCards = new ArrayList<Card>();
	
	public Deck()
	{		
		//	This method will do a for/each, returning each rank in the enum.
		DeckID=UUID.randomUUID();
		
		for (eRank Rank : eRank.values()) {
			Card heart=new Card(Rank,eSuit.HEARTS);
			Card club=new Card(Rank,eSuit.CLUBS);
			Card spade=new Card(Rank,eSuit.SPADES);
			Card diamond=new Card(Rank,eSuit.DIAMONDS);
			
			DeckCards.add(heart);
			DeckCards.add(club);
			DeckCards.add(spade);
			DeckCards.add(diamond);
					
			System.out.println(Rank.getiRankNbr());
		}
		
		Collections.shuffle(DeckCards);
	}
	
	public Deck(int numberOfJokers)
	{		
		//	This method will do a for/each, returning each rank in the enum.
		DeckID=UUID.randomUUID();
		
		for (eRank Rank : eRank.values()) {
			Card heart=new Card(Rank,eSuit.HEARTS);
			Card club=new Card(Rank,eSuit.CLUBS);
			Card spade=new Card(Rank,eSuit.SPADES);
			Card diamond=new Card(Rank,eSuit.DIAMONDS);
			
			DeckCards.add(heart);
			DeckCards.add(club);
			DeckCards.add(spade);
			DeckCards.add(diamond);
					
			System.out.println(Rank.getiRankNbr());
		}
		
		for(int i=0; i<numberOfJokers;i++)
		{
			DeckCards.add(new Card(eRank.JOKER, eSuit.CLUBS));
		}
		
		Collections.shuffle(DeckCards);
	}
	
	public Card DrawCard() throws DeckException
	{
		Card tempCard;
		
		if(DeckCards.isEmpty())
		{
			throw new DeckException(this);
		}
		else
		{
			tempCard=DeckCards.get(0);
		
			DeckCards.remove(0);
		}
		return tempCard;
	}
}
