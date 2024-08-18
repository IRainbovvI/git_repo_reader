package com.example.git_repo_reader.services;

import com.example.git_repo_reader.models.Branch;
import com.example.git_repo_reader.models.Owner;
import com.example.git_repo_reader.models.Repository;
import com.example.git_repo_reader.models.RepositoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GithubServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubService githubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNonForkReposByUsername() {
        String username = "testuser";
        Owner owner = new Owner();
        owner.setLogin(username);

        Repository repo1 = new Repository();
        repo1.setName("Repo1");
        repo1.setOwner(owner);
        repo1.setFork(false);

        Repository repo2 = new Repository();
        repo2.setName("Repo2");
        repo2.setOwner(owner);
        repo2.setFork(true);

        Repository[] mockResponse = {repo1, repo2};

        when(restTemplate.getForObject("https://api.github.com/users/" + username + "/repos", Repository[].class))
                .thenReturn(mockResponse);

        Branch branch = new Branch();
        branch.setName("main");
        Branch.Commit commit = new Branch.Commit();
        commit.setSha("test_sha");
        branch.setCommit(commit);
        Branch[] branches = {branch};

        when(restTemplate.getForObject("https://api.github.com/repos/testuser/Repo1/branches", Branch[].class))
                .thenReturn(branches);

        List<RepositoryDTO> result = githubService.getNonForkReposByUsername(username);

        assertEquals(1, result.size());
        assertEquals("Repo1", result.get(0).name());
        assertEquals("testuser", result.get(0).ownerLogin());
        assertEquals(1, result.get(0).branches().size());
        assertEquals("main", result.get(0).branches().get(0).getName());

        verify(restTemplate).getForObject("https://api.github.com/users/" + username + "/repos", Repository[].class);
        verify(restTemplate).getForObject("https://api.github.com/repos/testuser/Repo1/branches", Branch[].class);
    }

    @Test
    void testGetNonForkReposByUsername_UserNotFound() {
        String username = "nonexistentuser";

        when(restTemplate.getForObject("https://api.github.com/users/" + username + "/repos", Repository[].class))
                .thenThrow(HttpClientErrorException.NotFound.class);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            githubService.getNonForkReposByUsername(username);
        });

        assertEquals("User not found.", exception.getMessage());
    }


}
