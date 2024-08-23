# Contributing Guidelines

Thank you for considering contributing to the "afet-yonetim-sistemi" project! We appreciate your interest and welcome
any contributions that can help us improve the project.

Please take a moment to review these guidelines before making any contributions. Following these guidelines will help
you maintain a collaborative and inclusive environment for everyone involved.

## Table of Contents

- [How to Contribute](#how-to-contribute)
- [Reporting Issues](#reporting-issues)
- [Submitting Changes](#submitting-changes)
- [Code Style](#code-style)
- [Commit Messages](#commit-messages)
- [Testing](#testing)
- [Documentation](#documentation)
- [Community Guidelines](#community-guidelines)

## How to Contribute

Thank you for considering contributing to our project! To make the process smooth, please follow these steps:

1. **Join Our Discord Server:**
    - Join our [Discord server](https://discord.com/invite/NkAkYajkKa) to engage with the community and project
      maintainers.
    - Navigate to the 'Environment Informations' section and find the GitHub thread. Leave a message in the thread to
      express your interest in contributing.

2. **Review Jira Issues:**
    - Access
      our [Jira Issues](https://afetyonetimsistemi.atlassian.net/jira/software/c/projects/AYS/issues/AYS-408?filter=allissues&jql=project%20%3D%20%22AYS%22%20AND%20status%20%3D%20Ready%20AND%20assignee%20%3D%20empty%20ORDER%20BY%20created%20DESC)
      to view the issues and development tasks.
    - Identify relevant issues or features you would like to work on.

3. **Cloning the Project:**
    - Once added to the GitHub organization, clone the repository from the organization’s GitHub page.
    - Clone the repository using the command: `git clone https://github.com/afet-yonetim-sistemi/ays-be`

4. **Create a New Branch:**
    - Create a branch for your contribution based on the `main` branch.
    - Refer to the [Naming Conventions of Branches](#branch-naming-conventions) for guidance on branch naming.
    - Ensure your feature branch is associated with the relevant Jira issue and GitHub issue if applicable.

5. **Make Changes:**
   - Implement your changes in the branch you created.
   - Thoroughly test your changes to ensure they work as expected.

6. **Commit Changes:**
    - Commit your changes with clear and concise messages. Refer to the [Commit Messages](#commit-messages) section for
      guidance.

7. **Push Changes:**
    - Push your changes to the `main` branch of your cloned repository.

8. **Open a Pull Request (PR):**
    - Open a PR against the `main` branch of the original repository.
   - Provide a detailed description of your changes in the PR.
    - Link relevant Jira issues and GitHub issues in the PR description.
   - Follow the pull request naming conventions outlined below.
    - PR title should contain the Jira issue number and a brief description of the change.

9. **Review and Feedback:**
   - Once your PR is submitted, project maintainers will review it and provide feedback.
   - Be responsive to feedback and make necessary changes.

## Development Standards

Our project adheres to the [GitHub Flow](https://docs.github.com/en/get-started/quickstart/github-flow) for
collaboration.
Issues are managed on GitHub Discussions, with plans to transition them to Jira.

### Branch Naming Conventions:

- `bugfix/{jira-issue-number}/{optional-description}`
- `feature/{jira-issue-number}/{optional-description}`
- `refactor/{jira-issue-number}/{optional-description}`
- `hotfix/{jira-issue-number}/{optional-description}`

> Example : `feature/AYS-1/add-feature-to-handle-user-authentication`

### Pull Request Naming Conventions:

- `{jira-issue-number} | {header-for-summary-of-development}`

> Example : `AYS-1 | Add feature to handle user authentication`

Here are some additional standards to keep in mind:

- Link your feature branch to the corresponding GitHub issue.
- `main` branch is protected.
- PR should be linked to the relevant issue within the GitHub Project.
- Only one code owner's approval is needed for merging feature branches.
- PR should be squash-merged to avoid merge commit history.
- PR should pass build and necessary tests before merging.
- Releases should be tagged with a version.
- Commit messages should be subjectless.
- Resolve comments within the PR by the commenter.
- PR should be concise and address one thing.
- Avoid including secret/credential information.
- Code should comply with existing coding standards; no new standards should be introduced without justification.

## Reporting Issues

If you encounter any bugs, have feature requests, or want to provide general feedback, please feel free to open a
discussion
in the "afet-yonetim-sistemi" repository. When creating a discussion, please provide as much detail as possible,
including:

- A clear and descriptive title
- Steps to reproduce the issue
- Expected behavior
- Actual behavior
- Any error messages or logs

## Submitting Changes

When submitting changes, please ensure the following:

- Your code adheres to the project's [code style guidelines](#code-style).
- You have added appropriate tests for your changes (see [Testing](#testing)).
- The documentation has been updated, if necessary (see [Documentation](#documentation)).
- Your commits are clean and have clear commit messages (see [Commit Messages](#commit-messages)).

## Code Style

To maintain a consistent codebase, please follow the existing code style used in the project. If necessary, refer to any
code style guidelines mentioned in the repository.

## Commit Messages

Writing clear and descriptive commit messages is essential for the maintainability of the project. Follow these
guidelines when writing commit messages:

- **Be Clear and Concise:**
    - Ensure the subject line clearly summarizes the change. While it’s useful to start with a verb (e.g., "Fix," "
      Add," "Update"), the main goal is to make sure the message is clear and easily understandable.

- **Provide Additional Details:**
    - Include additional details in the commit message body if necessary.
    - Explain the reason for the change and provide context to help reviewers understand the impact and purpose of the
      change.

- **Squash and Merge:**
    - We use the Squash and Merge strategy for integrating pull requests. This method combines all commits in a pull
      request into a single commit before merging.
    - As a result, the pull request title becomes the final commit message. Ensure the title is descriptive and
      accurately reflects the overall change.
    - Example pull request title: `Enhance Error Handling in Payment Processing`

- **Example Commit Message:**
  ```
  Add new endpoint to handle user authentication

  - AYS-0 : Add new POST endpoint to handle user login
  - Include validation for user credentials
  - AYS-0 | Authentication Flow Has Been Refactored
  ```

By following these guidelines, you contribute to a clear and understandable project history, facilitating easier code
reviews and collaboration.

## Testing

If you are contributing code changes, it is crucial to include appropriate tests to ensure the stability and
functionality of the project. Follow these guidelines when writing tests:

- **Unit Tests:**
    - Write unit tests to cover the new code you have added or modified.
    - Ensure that each unit test is focused on a single aspect of the code to make it easy to understand and maintain.

- **Integration Tests:**
    - Include integration tests to verify that different parts of the system work together as expected.
    - Make sure your integration tests cover scenarios where multiple components interact.

- **Manual Tests:**
    - Use Postman to write and execute manual tests for API endpoints and ensure they perform as intended.
    - Document any manual test cases in a way that they can be easily replicated.

- **Test-Driven Development (TDD):**
    - When applicable, adopt the TDD approach. Write your tests before implementing the new code to ensure your changes
      meet the required functionality from the start.

- **Run Existing Test Suite:**
    - Run the existing test suite before submitting your changes to ensure they haven't introduced any regressions.
    - Address any failing tests that might be impacted by your changes.

- **Instructions for Running Tests:**
    - Provide clear instructions on how to run the tests, including any additional setup or specific commands needed.

By following these guidelines, you help maintain the quality and stability of the project while making it easier for
others to contribute and collaborate.

## Documentation

Clear and up-to-date documentation is crucial for the project's users and contributors. If your contribution introduces
new features, modifies existing functionality, or requires changes to the documentation, please make the necessary
updates.

Follow these guidelines for documenting your changes:

- Update the relevant documentation files to reflect your changes.
- Write clear and concise explanations.
- Use code examples or screenshots, if appropriate, to enhance understanding.
- Proofread your documentation to ensure it is accurate and free of errors.

## Community Guidelines

In addition to the technical aspects, it is important to follow the community guidelines to maintain a positive and
inclusive environment for all contributors.

- Be respectful and considerate of others' opinions and ideas.
- Be open to feedback and willing to collaborate with other contributors.
- Avoid personal attacks, offensive language, and harassment.
- If you notice any inappropriate behavior, report it to the project maintainers.

By adhering to these guidelines, we can work together to build a welcoming and productive community.

Thank you for your interest in contributing to the "afet-yonetim-sistemi" project! We appreciate your support and look
forward to your contributions.
