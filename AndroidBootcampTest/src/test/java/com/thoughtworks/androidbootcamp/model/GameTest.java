package com.thoughtworks.androidbootcamp.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        Attempt attempt = new Attempt(1, 2, "", 0);
        game.recordAttempt(treasure, attempt);
        assertThat(game.getAttemptForTreasure(treasure), is(attempt));
    }

    @Test
    public void shouldRetainTheBestAttempt() {
        Attempt attempt = new Attempt(1, 2, "", 0);
        attempt.setDistance(5);
        Attempt betterAttempt = new Attempt(4, 5, "", 0);
        betterAttempt.setDistance(3);
        game.recordAttempt(treasure, betterAttempt);
        game.recordAttempt(treasure, attempt);
        assertThat(game.getAttemptForTreasure(treasure), is(betterAttempt));

    }

    @Test
    public void shouldRetainTheNewerOfTwoEqualAttempts() {
        Attempt firstAttempt = new Attempt(1, 2, "", 0);
        firstAttempt.setDistance(5);
        Attempt secondAttempt = new Attempt(4, 5, "", 0);
        secondAttempt.setDistance(5);
        game.recordAttempt(treasure, firstAttempt);
        game.recordAttempt(treasure, secondAttempt);
        assertThat(game.getAttemptForTreasure(treasure), is(secondAttempt));

    }

    @Test
    public void shouldUnderstandHasPreviouslyAttemptedTreasure() {
        assertFalse(game.hasPreviouslyAttemptedTreasure(treasure));
        Attempt attempt = new Attempt(1, 2, "", 0);
        game.recordAttempt(treasure, attempt);
        assertTrue(game.hasPreviouslyAttemptedTreasure(treasure));
    }

    @Test
    public void shouldReturnDistanceDifferenceBetweenBestAndCurrentAttempt() {
        Attempt attempt = new Attempt(1, 2, "", 0);
        attempt.setDistance(5);
        Attempt betterAttempt = new Attempt(4, 5, "", 0);
        betterAttempt.setDistance(3);
        game.recordAttempt(treasure, betterAttempt);
        assertThat(game.recordAttempt(treasure, attempt), is(-2));
    }

    @Test
    public void shouldReturnAttemptsList() {
        Treasure treasure1 = mock(Treasure.class);
        Treasure treasure2 = mock(Treasure.class);
        Attempt attempt1 = mock(Attempt.class);
        Attempt attempt2 = mock(Attempt.class);
        Game game = gameWithTreasures(newArrayList(treasure1, treasure2));
        game.recordAttempt(treasure1, attempt1);
        game.recordAttempt(treasure2, attempt2);

        assertThat(game.getAttempts(), hasItems(attempt1, attempt2));
    }

    @Test
    public void shouldNotIncludeNullsInAttemptsList() {
        Treasure treasure1 = mock(Treasure.class);
        Treasure treasure2 = mock(Treasure.class);
        Attempt attempt1 = mock(Attempt.class);
        Game game = gameWithTreasures(newArrayList(treasure1, treasure2));
        game.recordAttempt(treasure1, attempt1);

        List<Attempt> expectedAttempts = newArrayList(attempt1);
        assertThat(game.getAttempts(), equalTo(expectedAttempts));
    }

    @Test
    public void shouldHaveNoTreasuresWhenCreated() {
        Game game = new Game();
        assertTrue(game.hasNoTreasures());
    }

    @Test
    public void shouldHaveTreasuresAfterSettingTreasures() {
        Game game = new Game();
        game.setTreasures(newArrayList(treasure));
        assertFalse(game.hasNoTreasures());
    }

    @Test
    public void shouldCalculateScoreBasedOnDistances() {
        Game game = gameWithAttemptsAtDistances(newArrayList(150));
        assertThat(game.getScore().getScore(), is(1000 - 150));
    }

    @Test
    public void shouldScoreZeroWhenDistanceIsGreaterThan1000() {
        Game game = gameWithAttemptsAtDistances(newArrayList(1500));
        assertThat(game.getScore().getScore(), is(0));
    }

    @Test
    public void shouldScoreZeroWhenTreasureIsNotAttempted() {
        ArrayList<Integer> distances = newArrayList();
        distances.add(null);
        Game game = gameWithAttemptsAtDistances(distances);
        assertThat(game.getScore().getScore(), is(0));
    }

    @Test
    public void shouldScoreRoundedAverageOverAllTreasures() {
        Game game = gameWithAttemptsAtDistances(newArrayList(500, null, 1000));
        assertThat(game.getScore().getScore(), is(166));
    }

    @Test
    public void shouldScoreZeroWhenNoTreasures() {
        ArrayList<Integer> emptyList = newArrayList();
        Game game = gameWithAttemptsAtDistances(emptyList);
        assertThat(game.getScore().getScore(), is(0));
    }

    private Game gameWithAttemptsAtDistances(ArrayList<Integer> distances) {
        Game game = new Game();
        int count = 0;
        for (Integer distance: distances) {
            Treasure treasure = mock(Treasure.class);
            Attempt attempt = null;
            if (distance != null) {
                attempt = new Attempt(1, 1, "", ++count);
                attempt.setDistance(distance);
            }
            game.recordAttempt(treasure, attempt);
        }
        return game;
    }

    private Game gameWithTreasures(List<Treasure> treasures) {
        Game game = new Game();
        game.setTreasures(newArrayList(treasures));
        return game;
    }

}
