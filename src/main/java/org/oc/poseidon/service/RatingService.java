package org.oc.poseidon.service;

import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository repo;

    public RatingService(RatingRepository repo) {
        this.repo = repo;
    }

    public List<Rating> ratingAll() {
        return repo.findAll();
    }

    public boolean validRating(Rating rating)
    {
        return (rating.getFitchRating() != null || rating.getSandPRating() != null || rating.getMoodysRating() != null || rating.getOrderNumber() != null);
    }

    public boolean addRating(Rating rating)
    {
        boolean result = false;

        if(validRating(rating))
        {
            repo.save(rating);
            result = true;
        }

        return result;
    }

    public Rating ratingById(int id)
    {
        return repo.findById(id);
    }

    public boolean updateRating(Rating formRating, int id)
    {
        boolean result = false;

        Rating rating = repo.findById(id);

        if(validRating(formRating)){

            rating.setFitchRating(formRating.getFitchRating());
            rating.setMoodysRating(formRating.getMoodysRating());
            rating.setSandPRating(formRating.getSandPRating());
            rating.setOrderNumber(formRating.getOrderNumber());

            repo.save(rating);

            result = true;
        }

        return result;
    }

    public void deleteRating(int id)
    {
        Rating rating = ratingById(id);
        repo.delete(rating);
    }

}
