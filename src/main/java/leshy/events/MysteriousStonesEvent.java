package leshy.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import leshy.LeshyMod;
import leshy.cards.abstracts.AbstractCreatureCard;
import leshy.cards.Stinkbug;
import leshy.cards.Stoat;
import leshy.cards.StuntedWolf;

import java.util.HashSet;

import static leshy.LeshyMod.makeEventPath;

public class MysteriousStonesEvent extends AbstractImageEvent {


    public static final String ID = LeshyMod.makeID("MysteriousStonesEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("MysteriousStonesEvent.png");

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;

    private boolean selectSacrifice = false;
    private boolean selectRecipient = false;

    private AbstractCreatureCard sacrifice;

    public MysteriousStonesEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if(!getSacrificeOptions(false).isEmpty())
            this.imageEventText.setDialogOption(OPTIONS[1]);
        else
            this.imageEventText.setDialogOption(OPTIONS[3], true);
        this.imageEventText.setDialogOption(OPTIONS[0]);


        this.hasDialog = true;
        this.hasFocus = true;
        this.combatTime = false;

    }

    @Override
    public void onEnterRoom() {
        GenericEventDialog.show();
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {

            //Intro screen
            case 0:
                switch (i) {
                    case 0:
                        selectSacrifice = true;
                        AbstractDungeon.gridSelectScreen.open(getSacrificeOptions(true), 1, "Select a creature to sacrifice.", false, false, false, false);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;

                    case 1:
                        openMap();
                        break;

                }
                break;

            case 1:
                selectRecipient = true;
                AbstractDungeon.gridSelectScreen.open(getRecipientOptions(sacrifice, true), 1, "Select a creature.", false, false, false, false);
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                this.imageEventText.clearRemainingOptions();
                screenNum = 2;
                break;

            case 2:
                resetNames();
                openMap();
                break;

        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.imageEventText.render(sb);
    }

    public void update() {
        super.update();

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && selectSacrifice) {
            selectSacrifice = false;
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            if(c instanceof AbstractCreatureCard) {
                sacrifice = (AbstractCreatureCard) c;
            }else{
                this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                this.imageEventText.clearRemainingOptions();
                screenNum = 2;
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && selectRecipient) {
            selectRecipient = false;
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            if(c instanceof AbstractCreatureCard) {
                ((AbstractCreatureCard) c).sigilOption(sacrifice);
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractCard copy = c.makeStatEquivalentCopy();
                if(copy instanceof Stoat){
                    switch (AbstractDungeon.miscRng.random(3)){
                        case 0:
                            copy.name = "WHAT AN HONOR";
                            ((Stoat) copy).titleScale = 1F;
                            break;
                        case 1:
                            copy.name = "FINE CHOICE";
                            ((Stoat) copy).titleScale = 1F;
                            break;
                        case 2:
                            copy.name = "YEAH? ALRIGHT";
                            ((Stoat) copy).titleScale = 1F;
                            break;
                        case 3:
                            copy.name = "ANOTHER SIGIL...HOORAY...";
                            ((Stoat) copy).titleScale = 0.6F;
                            break;
                    }
                }
                if (copy instanceof Stinkbug){
                    switch (AbstractDungeon.miscRng.random(1)){
                        case 0:
                            copy.name = "EMPOWER ME!";
                            ((Stinkbug) copy).titleScale = 1F;
                            break;
                        case 1:
                            copy.name = "YES! MORE POWER!";
                            ((Stinkbug) copy).titleScale = 0.8F;
                            break;
                    }
                }
                if (copy instanceof StuntedWolf){
                    switch (AbstractDungeon.miscRng.random(1)){
                        case 0:
                            copy.name = "A TASTE OF MY FORMER GLORY";
                            ((StuntedWolf) copy).titleScale = 0.6F;
                            break;
                        case 1:
                            copy.name = "VERY WELL";
                            ((StuntedWolf) copy).titleScale = 1F;
                            break;
                    }
                }
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(copy));
                resetNames();

            }else{
                this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                this.imageEventText.clearRemainingOptions();
                screenNum = 2;
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }


    }

    public static CardGroup getSacrificeOptions(boolean changeName){
        CardGroup list = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group){
            if(c instanceof AbstractCreatureCard && !((AbstractCreatureCard) c).innate.isEmpty() && ((AbstractCreatureCard) c).gained.isEmpty() && !getRecipientOptions((AbstractCreatureCard) c, false).isEmpty()){
                if(changeName){
                    if (c instanceof Stinkbug){

                        switch (AbstractDungeon.miscRng.random(3)){
                            case 0:
                                c.name = "THE CHOICE IS YOURS";
                                ((Stinkbug) c).titleScale = 0.7F;
                                break;
                            case 1:
                                c.name = "DO WHAT YOU MUST";
                                ((Stinkbug) c).titleScale = 0.7F;
                                break;
                            case 2:
                                c.name = "HELLO";
                                ((Stinkbug) c).titleScale = 1F;
                                break;
                            case 3:
                                c.name = "AM I YOUR CHOICE?";
                                ((Stinkbug) c).titleScale = 0.7F;
                                break;
                        }

                    }
                }
                list.addToTop(c);
            }

        }
        return list;
    }

    public static CardGroup getRecipientOptions(AbstractCreatureCard sacrifice, boolean changeName){
        CardGroup list = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if(c instanceof AbstractCreatureCard){
                HashSet<AbstractCreatureCard.Sigils> joinInnates = new HashSet<>();
                joinInnates.addAll(((AbstractCreatureCard) c).innate);
                joinInnates.addAll(sacrifice.innate);
                if ((((AbstractCreatureCard) c).gained.isEmpty() && !(joinInnates.equals(((AbstractCreatureCard) c).innate)))
                        || c instanceof Stoat){
                    if(changeName){
                        if (c instanceof Stoat) {
                            switch (AbstractDungeon.miscRng.random(3)){
                                case 0:
                                    c.name = "YOUR CHOICE";
                                    ((Stoat) c).titleScale = 1F;
                                    break;
                                case 1:
                                    c.name = "I'LL GO";
                                    ((Stoat) c).titleScale = 1F;
                                    break;
                                case 2:
                                    c.name = "IT'S ME";
                                    ((Stoat) c).titleScale = 1F;
                                    break;
                                case 3:
                                    c.name = "PICK ME";
                                    ((Stoat) c).titleScale = 1F;
                                    break;
                            }
                        }
                        if(c instanceof Stinkbug){
                            switch (AbstractDungeon.miscRng.random(3)){
                                case 0:
                                    c.name = "THE CHOICE IS YOURS";
                                    ((Stinkbug) c).titleScale = 0.7F;
                                    break;
                                case 1:
                                    c.name = "MARVELOUS";
                                    ((Stinkbug) c).titleScale = 1F;
                                    break;
                                case 2:
                                    c.name = "HELLO";
                                    ((Stinkbug) c).titleScale = 1F;
                                    break;
                                case 3:
                                    c.name = "AM I YOUR CHOICE?";
                                    ((Stinkbug) c).titleScale = 0.7F;
                                    break;
                            }
                        }
                        if(c instanceof StuntedWolf){
                            switch (AbstractDungeon.miscRng.random(4)){
                                case 0:
                                    c.name = "AHEM";
                                    ((StuntedWolf) c).titleScale = 1F;
                                    break;
                                case 1:
                                    c.name = "I AM AT YOUR SERVICE";
                                    ((StuntedWolf) c).titleScale = 0.7F;
                                    break;
                                case 2:
                                    c.name = "I AM YOURS";
                                    ((StuntedWolf) c).titleScale = 1F;
                                    break;
                                case 3:
                                    c.name = "...";
                                    ((StuntedWolf) c).titleScale = 1F;
                                    break;
                                case 4:
                                    c.name = "SELECT WISELY";
                                    ((StuntedWolf) c).titleScale = 0.8F;
                                    break;
                            }
                        }
                    }
                    list.addToTop(c);
                }
            }
        }
        return list;
    }

    public static void resetNames(){
        for(AbstractCard deck : AbstractDungeon.player.masterDeck.group)
            if(deck instanceof Stoat || deck instanceof Stinkbug || deck instanceof StuntedWolf){
                ((AbstractCreatureCard) deck).updateName();
                ((AbstractCreatureCard) deck).titleScale = 1F;
            }
    }


}
