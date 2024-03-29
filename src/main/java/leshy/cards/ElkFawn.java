package leshy.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import leshy.LeshyMod;
import leshy.actions.CreatureSacrificeAction;
import leshy.actions.SummonCreatureAction;
import leshy.cards.abstracts.AbstractCreatureCard;
import leshy.characters.Leshy;
import leshy.orbs.CreatureOrb;

import static leshy.LeshyMod.makeCardPath;

public class ElkFawn extends AbstractCreatureCard {


    public static final String ID = LeshyMod.makeID(ElkFawn.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ElkFawn.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = Leshy.Enums.CREATURE;
    public static final CardColor COLOR = Leshy.Enums.LESHY_BROWN;

    private static final int COST = -2;

    public ElkFawn() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        sigilImgPath = makeCardPath("ElkFawn_Sigil.png");

        tribe = CreatureTribe.HOOVED;

        costType = CreatureCostType.BLOOD;
        extraCost = 1;

        attack = baseAttack = trueBaseAttack = 2;

        health = baseHealth = trueBaseHealth = 8;

        innate.add(Sigils.FLEDGLING);
        innate.add(Sigils.RAMPAGER);
        current.addAll(innate);

        cardsToPreview = new Elk();

        initializeDescription();

    }

    @Override
    public void onStartOfTurn() {
        super.onStartOfTurn();
        Elk elk = new Elk();
        elk.transform(this);
        elk.baseAttack += (this.baseAttack - this.trueBaseAttack);
        elk.baseHealth += (this.baseHealth - this.trueBaseHealth);
        this.isTransforming = true;
        elk.transformed = true;
        elk.isFrail = this.isFrail;
        CreatureOrb orb = new CreatureOrb(elk);
        orb.damageTaken = this.orb.damageTaken;
        addToBot(new CreatureSacrificeAction(this.orb));
        addToBot(new SummonCreatureAction(orb, AbstractDungeon.player.orbs.indexOf(this.orb)));
    }



    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

}
