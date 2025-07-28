package org.oc.poseidon;

import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.repositories.BidListRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Tests d'intégration sur le repository BidListRepository.
 * Vérifie les opérations CRUD : création, mise à jour, lecture et suppression.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BidTests {

	@Autowired
	private BidListRepository bidListRepository;

	@Test
	public void bidListTest() {
		// --- Création ---
		BidList bid = new BidList("Account Test", "Type Test", 10d);
		bid = bidListRepository.save(bid);

		Assert.assertNotNull("L'ID ne doit pas être nul après sauvegarde", bid.getBidListId());
		Assert.assertEquals("La quantité de bid doit être 10", 10d, bid.getBidQuantity(), 0.001);

		// --- Mise à jour ---
		bid.setBidQuantity(20d);
		bid = bidListRepository.save(bid);

		Assert.assertEquals("La quantité de bid doit avoir été mise à jour à 20", 20d, bid.getBidQuantity(), 0.001);

		// --- Lecture ---
		List<BidList> listResult = bidListRepository.findAll();
		Assert.assertFalse("La liste des BidList ne doit pas être vide", listResult.isEmpty());

		// --- Suppression ---
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidList> bidList = bidListRepository.findById(id);

		Assert.assertFalse("Le BidList supprimé ne doit plus être présent", bidList.isPresent());
	}
}
