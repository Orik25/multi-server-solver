package com.orik.clientserver.service.impl;

import com.orik.clientserver.DAO.RequestRepository;
import com.orik.clientserver.DTO.request.RequestConverterDTO;
import com.orik.clientserver.DTO.request.RequestDTO;
import com.orik.clientserver.DTO.request.StatusRequestDTO;
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
    public Request findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(()-> new NoRequestFoundException("Request not found with id: " + id));
    }

    @Override
    public Request update(StatusRequestDTO statusRequestDTO) {
        Request request = requestRepository.findById(statusRequestDTO.getId())
                .orElseThrow(()-> new NoRequestFoundException("Impossible to update the Request. Request not found with id: " + statusRequestDTO.getId()));
        String status = statusRequestDTO.getStatus();
        Long timeLeft = statusRequestDTO.getTimeLeft();
        if(timeLeft != null){
            request.setStatus(status+"("+timeLeft+"sek.)");
        }
        else {
            request.setStatus(status);
        }
        Long result = statusRequestDTO.getResult();
        if(result != null){
            request.setResult(result);
        }
        return requestRepository.save(request);
    }

    @Override
    public Request update(RequestDTO requestDTO) {
        Request request = requestRepository.findById(requestDTO.getId())
                .orElseThrow(()-> new NoRequestFoundException("Impossible to update the Request. Request not found with id: " + requestDTO.getId()));
        String status = requestDTO.getStatus();
        request.setStatus(status);
        return requestRepository.save(request);
    }

    @Override
    public void deleteById(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(()-> new NoRequestFoundException("Impossible to delete the Request. Request not found with id: " + id));
        requestRepository.delete(request);
    }

}