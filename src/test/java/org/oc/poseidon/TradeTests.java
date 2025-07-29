package org.oc.poseidon;

import org.oc.poseidon.domain.Trade;
import org.oc.poseidon.repositories.TradeRepository;
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
public class TradeTests {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeEntityCrudOperationsShouldWork() {
		// Création d’un Trade
		Trade trade = new Trade("Trade Account", "Type");

		// Save
		trade = tradeRepository.save(trade);
		Assert.assertNotNull("L'ID ne doit pas être null après sauvegarde", trade.getTradeId());
		Assert.assertEquals("Trade Account", trade.getAccount());

		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeRepository.save(trade);
		Assert.assertEquals("Trade Account Update", trade.getAccount());

		// Find
		List<Trade> trades = tradeRepository.findAll();
		Assert.assertFalse("La liste des trades ne doit pas être vide", trades.isEmpty());

		// Delete
		Integer id = trade.getTradeId();
		tradeRepository.delete(trade);
		Optional<Trade> deletedTrade = tradeRepository.findById(id);
		Assert.assertFalse("Le trade devrait avoir été supprimé", deletedTrade.isPresent());
	}
}
