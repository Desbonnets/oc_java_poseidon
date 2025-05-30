package org.oc.poseidon.service;

import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointService {

    private final CurvePointRepository repo;

    public CurvePointService(CurvePointRepository repo) {
        this.repo = repo;
    }

    public List<CurvePoint> curveAll() {
        return repo.findAll();
    }

    public boolean validCurvePoint(CurvePoint curvePoint)
    {
        return (curvePoint.getCurveId() != null) &&
                (!curvePoint.getValue().isNaN() || !curvePoint.getTerm().isNaN());
    }

    public boolean addCurvePoint(CurvePoint curvePoint)
    {
        boolean result = false;

        if(validCurvePoint(curvePoint))
        {
            repo.save(curvePoint);
            result = true;
        }

        return result;
    }

    public CurvePoint curvePointById(int id)
    {
        return repo.findById(id);
    }

    public boolean updateCurvePoint(CurvePoint formCurvePoint, int id)
    {
        boolean result = false;

        CurvePoint curvePoint = repo.findById(id);

        if(validCurvePoint(formCurvePoint)){

            curvePoint.setCurveId(formCurvePoint.getCurveId());
            curvePoint.setTerm(formCurvePoint.getTerm());
            curvePoint.setValue(formCurvePoint.getValue());

            repo.save(curvePoint);

            result = true;
        }

        return result;
    }

}
