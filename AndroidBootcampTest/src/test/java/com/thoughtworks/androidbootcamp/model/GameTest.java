package com.thoughtworks.androidbootcamp.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;

public class GameTest {

    private Game game;
    private Treasure treasure;

    @Before
    public void setUp() throws Exception {
        treasure = mock(Treasure.class);
        game = gameWithTreasures(newArrayList(treasure));
    }

    @Test
    public void shouldHaveAListOfTreasures() {
        assertThat(game.getTreasures().size(), is(1));
    }

    @Test
    public void shouldBeAbleToRecordAttempts() {
        assertThat(game.getAttemptForTreasure(treasure), is(nullValue()));
        Attempt attempt = new Attempt(1, 2, "");
        game.recordAttempt(treasure, attempt);
        assertThat(game.getAttemptForTreasure(treasure), is(attempt));
    }

    @Test
    public void shouldRetainTheBestAttempt() {
        Attempt attempt = new Attempt(1, 2, "");
        attempt.setDistance(5);
        Attempt betterAttempt = new Attempt(4, 5, "");
        betterAttempt.setDistance(3);
        game.recordAttempt(treasure, betterAttempt);
        game.recordAttempt(treasure, attempt);
        assertThat(game.getAttemptForTreasure(treasure), is(betterAttempt));

    }

    @Test
    public void shouldRetainTheNewerOfTwoEqualAttempts() {
        Attempt firstAttempt = new Attempt(1, 2, "");
        firstAttempt.setDistance(5);
        Attempt secondAttempt = new Attempt(4, 5, "");
        secondAttempt.setDistance(5);
        game.recordAttempt(treasure, firstAttempt);
        game.recordAttempt(treasure, secondAttempt);
        assertThat(game.getAttemptForTreasure(treasure), is(secondAttempt));

    }

    @Test
    public void shouldUnderstandHasPreviouslyAttemptedTreasure() {
        assertFalse(game.hasPreviouslyAttemptedTreasure(treasure));
        Attempt attempt = new Attempt(1, 2, "");
        game.recordAttempt(treasure, attempt);
        assertTrue(game.hasPreviouslyAttemptedTreasure(treasure));
    }

    @Test
    public void shouldReturnDistanceDifferenceBetweenBestAndCurrentAttempt() {
        Attempt attempt = new Attempt(1, 2, "");
        attempt.setDistance(5);
        Attempt betterAttempt = new Attempt(4, 5, "");
        betterAttempt.setDistance(3);
        game.recordAttempt(treasure, betterAttempt);
        assertThat(game.recordAttempt(treasure, attempt), is(-2));
    }

    private Game gameWithTreasures(List<Treasure> treasures) {
        Game game = new Game();
        game.setTreasures(newArrayList(treasures));
        return game;
    }

}
