package org.oc.poseidon.service;

import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurveService {

    private final CurvePointRepository repo;

    public CurveService(CurvePointRepository repo) {
        this.repo = repo;
    }

    public List<CurvePoint> curveAll() {
        return repo.findAll();
    }

}
