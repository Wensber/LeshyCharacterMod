package leshy.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import leshy.LeshyMod;
import leshy.cards.abstracts.AbstractCreatureCard;
import leshy.cards.abstracts.AbstractTotemBaseCard;
import leshy.characters.Leshy;

import static leshy.LeshyMod.makeCardPath;

public class FecundityTotem extends AbstractTotemBaseCard {


    public static final String ID = LeshyMod.makeID(FecundityTotem.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("FecundityTotem.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Leshy.Enums.LESHY_BROWN;


    public FecundityTotem() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        base = AbstractCreatureCard.Sigils.FECUNDITY;

    }




}
