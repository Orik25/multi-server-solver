package com.orik.clientserver.service.impl;

import com.orik.clientserver.DAO.RequestRepository;
import com.orik.clientserver.constant.RequestStatus;
import com.orik.clientserver.entities.Request;
import com.orik.clientserver.exception.NoRequestFoundException;
import com.orik.clientserver.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Page<Request> getAllRequestsSorted(int page, int size, String sortField, String sortOrder,Long userId) {
        Sort sort = Sort.by(sortField);

        if ("desc".equals(sortOrder)) {
            sort = sort.descending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return requestRepository.findByUserId(userId,pageRequest);
    }

    @Override
    public Request addNew(Request request) {
       return requestRepository.save(request);
    }

    @Override
    public void deleteById(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(()-> new NoRequestFoundException("Impossible to update the Request. Request not found with id: " + id));
        requestRepository.delete(request);
    }

}