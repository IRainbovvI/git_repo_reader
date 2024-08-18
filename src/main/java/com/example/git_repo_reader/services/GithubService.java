package com.example.git_repo_reader.services;

import com.example.git_repo_reader.models.Branch;
import com.example.git_repo_reader.models.Repository;
import com.example.git_repo_reader.models.RepositoryDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GithubService {

    private final RestTemplate restTemplate;
    private static final String GITHUB_API_URL = "https://api.github.com";

    public GithubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepositoryDTO> getNonForkReposByUsername(String username) {
        try {
            String url = GITHUB_API_URL + "/users/" + username + "/repos";
            Repository[] repositories = restTemplate.getForObject(url, Repository[].class);

            if (repositories == null) {
                throw new RuntimeException("User not found or no repositories found.");
            }

            return List.of(repositories).stream()
                    .filter(repo -> !repo.isFork())
                    .peek(this::populateBranches)
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User not found.");
        }
    }

    private RepositoryDTO convertToDto(Repository repository) {
        return new RepositoryDTO(repository.getName(),repository.getOwner().getLogin(),  repository.getBranches());
    }

    private void populateBranches(Repository repository) {
        String url = GITHUB_API_URL + "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/branches";
        Branch[] branches = restTemplate.getForObject(url, Branch[].class);
        repository.setBranches(branches != null ? List.of(branches) : new ArrayList<>());
    }

}
