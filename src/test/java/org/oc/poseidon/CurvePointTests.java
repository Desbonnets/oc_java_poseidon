package org.oc.poseidon;

import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.repositories.CurvePointRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Tests d'intégration sur le repository CurvePointRepository.
 * Vérifie les opérations CRUD : création, mise à jour, lecture et suppression.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CurvePointTests {

	@Autowired
	private CurvePointRepository curvePointRepository;

	@Test
	public void curvePointTest() {
		// --- Création ---
		CurvePoint curvePoint = new CurvePoint(10, 10d, 30d);
		curvePoint = curvePointRepository.save(curvePoint);

		Assert.assertNotNull("L'ID du CurvePoint doit être généré après l'enregistrement", curvePoint.getId());
		Assert.assertEquals("Le curveId doit être 10", 10, curvePoint.getCurveId().intValue());

		// --- Mise à jour ---
		curvePoint.setCurveId(20);
		curvePoint = curvePointRepository.save(curvePoint);

		Assert.assertEquals("Le curveId doit avoir été mis à jour à 20", 20, curvePoint.getCurveId().intValue());

		// --- Lecture ---
		List<CurvePoint> listResult = curvePointRepository.findAll();
		Assert.assertFalse("La liste des CurvePoints ne doit pas être vide", listResult.isEmpty());

		// --- Suppression ---
		Integer id = curvePoint.getId();
		curvePointRepository.delete(curvePoint);
		Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);

		Assert.assertFalse("Le CurvePoint supprimé ne doit plus exister en base", curvePointList.isPresent());
	}
}
