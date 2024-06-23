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

1. **Fork the Repository:**
   - Visit our [GitHub repository](https://github.com/afet-yonetim-sistemi/ays-be) and fork it to your GitHub account.

2. **Create a New Branch:**
   - Create a branch for your contribution by referring to
     the [Naming Conventions of Branches](#branch-naming-conventions) for guidance.
   - Ensure your feature branch is associated with the corresponding GitHub issue from the 'Development' section in
     each issue page.

3. **Make Changes:**
   - Implement your changes in the branch you created.
   - Thoroughly test your changes to ensure they work as expected.

4. **Commit Changes:**
   - Commit your changes with clear and concise messages. Refer to the [Commit Messages](#commit-messages) section for
     guidance.

5. **Push Changes:**
   - Push your changes to your forked repository.

6. **Open a Pull Request (PR):**
   - Open a PR against the `main` branch of the original repository by referring to
     the [Pull Request Naming Conventions](#pull-request-naming-conventions) for guidance.
   - Provide a detailed description of your changes in the PR.
   - Link relevant issues or discussions in the PR description.
   - Follow the pull request naming conventions outlined below.
   - PR title should contain the issue/feature/bug number.

7. **Review and Feedback:**
   - Once your PR is submitted, project maintainers will review it and provide feedback.
   - Be responsive to feedback and make necessary changes.

## Development Standards

Our project adheres to the [GitHub flow](https://docs.github.com/en/get-started/quickstart/github-flow) for
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

- Limit the subject line to 50 characters.
- Begin the subject line with a verb in the present tense (e.g., "Fix," "Add," "Update").
- Use the imperative mood (e.g., "Fix bug" instead of "Fixed bug").
- Provide additional details in the commit message body if necessary. Explain why the change was made and any relevant
  information for reviewers.

Example:

```
Add feature to handle user authentication

- Implemented a new module for user authentication
- Updated existing login page UI
- Added new API endpoint for user registration
```

## Testing

If you are contributing code changes, it is important to include appropriate tests to ensure the stability and
functionality of the project. Follow these guidelines when writing tests:

- Write tests that cover the new code you have added or modified.
- Ensure your tests are easy to understand and maintain.
- Run the existing test suite before submitting your changes to make sure they haven't introduced any regressions.
- Provide instructions on how to run the tests if they require additional setup or specific commands.

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
