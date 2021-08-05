package com.example.animesocialapp.recommendationManagement;

import com.example.animesocialapp.animeManagment.Anime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class QuickSort {

    private List<Anime> animeList;

    public QuickSort(List<Anime> animeList) {
        this.animeList = animeList;

        List<String> animeTitles = new ArrayList<>();
        List<Integer> animeScores = new ArrayList<>();

        for (Anime anime : animeList) {
            animeTitles.add(anime.getTitle());
            animeScores.add(anime.getRecScore());
        }
        Timber.i("Unsorted Titles: " + String.valueOf(animeTitles));
        Timber.i("Unsorted Scores: " + String.valueOf(animeScores));

    }

    public void quickSort(int left, int right) {
        if (right - left <= 0) {
            return ;
        } else {
            int pivot = animeList.get(right).getRecScore();

            Timber.i("Value in right " + animeList.get(right).getRecScore() + " is made the pivot");

            Timber.i("left: " + left + " right: " + right + " pivot: " + pivot + " sent to be partioned");

            int pivotLocation = partitionArray(left, right, pivot);

            Timber.i("Value in left " + animeList.get(left).getRecScore() + " is made the pivot");

            quickSort(left, pivotLocation - 1);

            quickSort(pivotLocation + 1, right);
        }
    }

    public int partitionArray(int left, int right, int pivot) {

        int leftPointer = left - 1;
        int rightPointer = right;

        while(true) {

            while (animeList.get(++leftPointer).getRecScore() > pivot)
                ;

            Timber.i(animeList.get(leftPointer).getRecScore() + " in index " + leftPointer + " is smaller than the pivot value " + pivot);

            while (animeList.get(--rightPointer).getRecScore() < pivot)
                ;

            Timber.i(animeList.get(rightPointer).getRecScore() + " in index " + rightPointer + " is bigger than the pivot value " + pivot);

            if(leftPointer >= rightPointer) {
                break;
            } else {

                Collections.swap(animeList, leftPointer, rightPointer);

                Timber.i(animeList.get(leftPointer).getRecScore() + " was swapped for " + animeList.get(rightPointer).getRecScore());

            }

        }

        Collections.swap(animeList, leftPointer, right);

        return leftPointer;
    }

}
