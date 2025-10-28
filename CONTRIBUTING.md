# Contributing to SaaS Starter Kit

Thank you for your interest in contributing to the SaaS Starter Kit! We welcome contributions from the community and are excited that you want to help make this project better.

## 🎉 Hacktoberfest Welcome!

This project participates in Hacktoberfest. We welcome contributions of all kinds!

### Getting Started for Hacktoberfest

1. **Fork the repository**
2. **Check existing issues** or create a new one
3. **Submit a PR** that solves an issue or adds value
4. **Maintainers will review** and merge your contribution

## How to Contribute

### Reporting Bugs

If you find a bug, please open an issue with:

- Clear description of the problem
- Steps to reproduce
- Expected vs actual behavior
- Environment details (Java version, OS, etc.)

### Suggesting Enhancements

We love new ideas! Open an issue to suggest:

- New features
- Improvements to existing features
- Documentation updates
- Performance optimizations

### Pull Requests

1. **Fork the repository** and clone locally
2. **Create a feature branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes** and test thoroughly
4. **Commit** with clear messages:
   ```bash
   git commit -m "Add: Description of your changes"
   ```
5. **Push** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
6. **Open a Pull Request** and describe your changes

### Code Style Guidelines

- Follow existing code structure and naming conventions
- Write meaningful commit messages
- Add comments for complex logic
- Update documentation when necessary
- Ensure all tests pass

### Commit Message Format

Use clear, descriptive commit messages:

- `Add: New feature`
- `Fix: Bug description`
- `Update: What was updated`
- `Refactor: What was refactored`
- `Docs: Documentation update`

## Development Setup

### Prerequisites

- Java 17 or higher
- PostgreSQL 12+
- Gradle 8+
- IDE (IntelliJ IDEA, VS Code, or Eclipse)

### Setup Steps

1. Clone your fork:

   ```bash
   git clone https://github.com/YOUR_USERNAME/saas-starter-kit.git
   cd saas-starter-kit
   ```

2. Create PostgreSQL database:

   ```sql
   CREATE DATABASE saas_starter;
   ```

3. Update `application.yml` with your database credentials

4. Build the project:

   ```bash
   ./gradlew build
   ```

5. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Areas for Contribution

### 🐛 Bug Fixes

- Fix any open bugs in the issue tracker
- Improve error handling

### ✨ New Features

- Add new authentication methods
- Implement additional OAuth providers
- Add new subscription management features
- Enhance multi-tenant capabilities

### 📚 Documentation

- Improve README
- Add code comments
- Write tutorials
- Create video demos

### 🧪 Testing

- Add unit tests
- Add integration tests
- Improve test coverage

### 🎨 UI/UX (Frontend)

- Create admin panel UI
- Design login pages
- Add dashboard components

### 🔒 Security

- Security enhancements
- Vulnerability fixes
- Security documentation

## What Makes a Good Contribution?

✅ **Good contributions**:

- Well-tested code
- Clear documentation
- Follows existing patterns
- Solves a real problem
- Clear commit messages

❌ **Avoid**:

- Duplicate PRs for same issue
- Trivial changes (typos in comments)
- Breaking changes without discussion
- Code without tests

## Review Process

1. All PRs require at least one maintainer review
2. We aim to review within 48 hours
3. Address feedback promptly
4. Maintainers will merge once approved

## Code of Conduct

### Our Pledge

We pledge to make participation in our project a harassment-free experience for everyone, regardless of age, body size, disability, ethnicity, gender identity and expression, level of experience, nationality, personal appearance, race, religion, or sexual identity and orientation.

### Our Standards

**Be respectful**: Treat everyone with respect
**Be inclusive**: Welcome newcomers
**Be collaborative**: Help others learn and grow
**Be constructive**: Provide helpful feedback

## Getting Help

- Open an issue for bugs or questions
- Check existing documentation
- Review closed issues for similar problems
- Reach out to maintainers

## Recognition

Contributors will be:

- Listed in the project README
- Mentioned in release notes
- Given proper credit for their work

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

**Happy Coding! 🚀**
