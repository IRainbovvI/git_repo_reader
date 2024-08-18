package com.example.git_repo_reader.controllers;

import com.example.git_repo_reader.models.RepositoryDTO;
import com.example.git_repo_reader.services.GithubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/repos/{username}")
    public ResponseEntity<?> getRepos(@PathVariable String username, @RequestHeader("Accept") String acceptHeader) {
        if (!"application/json".equals(acceptHeader)) {
            return new ResponseEntity<>("Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        List<RepositoryDTO> repositories = githubService.getNonForkReposByUsername(username);
        return new ResponseEntity<>(repositories, HttpStatus.OK);

    }
}
