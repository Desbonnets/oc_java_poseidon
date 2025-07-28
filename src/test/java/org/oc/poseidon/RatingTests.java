package org.oc.poseidon;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Test d'intégration de la classe Rating et de RatingRepository.
 * Ce test couvre les opérations de base : création, mise à jour, récupération et suppression.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingTests {

	@Autowired
	private RatingRepository ratingRepository;

	@Test
	public void ratingEntityCrudOperationsShouldWork() {
		// Création d'une entité Rating
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);

		// Sauvegarde
		rating = ratingRepository.save(rating);
		Assert.assertNotNull("L'ID ne doit pas être null après un save", rating.getId());
		Assert.assertEquals(10, (int) rating.getOrderNumber());

		// Mise à jour
		rating.setOrderNumber(20);
		rating = ratingRepository.save(rating);
		Assert.assertEquals(20, (int) rating.getOrderNumber());

		// Récupération
		List<Rating> listResult = ratingRepository.findAll();
		Assert.assertFalse("La liste des ratings ne doit pas être vide", listResult.isEmpty());

		// Suppression
		Integer id = rating.getId();
		ratingRepository.delete(rating);
		Optional<Rating> deletedRating = ratingRepository.findById(id);
		Assert.assertFalse("L'entité rating devrait avoir été supprimée", deletedRating.isPresent());
	}
}
