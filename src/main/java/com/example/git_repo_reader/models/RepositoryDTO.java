package com.example.git_repo_reader.models;

import java.util.List;

public record RepositoryDTO(String name, String ownerLogin, List<Branch> branches) {}
