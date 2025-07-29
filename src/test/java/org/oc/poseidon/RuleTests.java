package org.oc.poseidon;

import org.oc.poseidon.domain.RuleName;
import org.oc.poseidon.repositories.RuleNameRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTests {

	@Autowired
	private RuleNameRepository ruleNameRepository;

	@Test
	public void ruleNameEntityCrudOperationsShouldWork() {
		// Création d'une entité RuleName
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Save
		rule = ruleNameRepository.save(rule);
		Assert.assertNotNull("L'ID ne doit pas être null après un save", rule.getId());
		Assert.assertEquals("Rule Name", rule.getName());

		// Update
		rule.setName("Rule Name Update");
		rule = ruleNameRepository.save(rule);
		Assert.assertEquals("Rule Name Update", rule.getName());

		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
		Assert.assertFalse("La liste des règles ne doit pas être vide", listResult.isEmpty());

		// Delete
		Integer id = rule.getId();
		ruleNameRepository.delete(rule);
		Optional<RuleName> deletedRule = ruleNameRepository.findById(id);
		Assert.assertFalse("L'entité RuleName devrait avoir été supprimée", deletedRule.isPresent());
	}
}
