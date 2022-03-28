package com.sample.excel.exceldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sample.excel.exceldemo.model.NotesModel;

import reactor.core.publisher.Mono;

@Service
public class ApiService {

	@Autowired
	private WebClient webClient;

	public NotesModel[] getApiResults() {

		Mono<NotesModel[]> response = webClient.get().uri("/posts").retrieve().bodyToMono(NotesModel[].class);

		return response.block();

	}

}
