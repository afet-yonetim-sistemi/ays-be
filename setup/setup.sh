#!/bin/bash
echo "\033[96m
     ___  ____    ____  _______,
    /   \ \   \  /   / /       |
   /  ^  \ \   \/   / |   (----'
  /  /_\  \ \_    _/   \   \\
 /  _____  \  |  | .----)   |
/__/     \__\ |__| |_______/
:: AYS | Afet Yönetim Sistemi ::\033[0m
"

FILE_NAME="settings.xml"
IS_OVERWRITE=true
if [[ -f "$FILE_NAME" ]]; then
  echo "⚠️ \033[93m$FILE_NAME file already exists. If you want to update your credentials, you can overwrite it now.\033[0m"
  while true; do
  echo "\033[97m"
  read -p "➡️ Do you want to overwrite the existing $FILE_NAME file? (y/n): " choice
  case "$choice" in
    [Yy]* )
      IS_OVERWRITE=true
      echo ""
      echo ""
      break
      ;;
    [Nn]* )
      IS_OVERWRITE=false
      break
      ;;
    * )
      echo "Invalid choice, please enter 'y' or 'n'."
      ;;
  esac
done
fi

if [[ "$IS_OVERWRITE" == true ]]; then
  echo "\033[96mPlease provide the following values for setup 🚀\033[0m"
  while true; do
    echo "\033[97m"
    read -p "➡️ Enter your GitHub username: " YOUR_GITHUB_USERNAME
    if [[ -n "$YOUR_GITHUB_USERNAME" ]]; then
      break
    fi
    echo "\033[41mPlease enter your GitHub username.\033[0m"
  done

  while true; do
    echo "\033[97m"
    read -p "➡️ Do you already have a GitHub Access Token? (y/n): " has_token
    case "$has_token" in
      [Yy]* )
        while true; do
          echo "\033[97m"
          read -p "➡️ Enter your GitHub Access Token: " YOUR_PERSONAL_GITHUB_ACCESS_TOKEN
          if [[ -n "$YOUR_PERSONAL_GITHUB_ACCESS_TOKEN" ]]; then
            break
          fi
          echo "\033[41mPlease enter your GitHub Access Token.\033[0m"
        done
        break
        ;;
      [Nn]* )
        echo "\033[96m"
        read -p "Press Enter to open browser for creating a new Personal Access Token:" _
        echo "\033[0m"
        if command -v xdg-open >/dev/null; then
          xdg-open "https://github.com/settings/tokens/new"
        elif command -v open >/dev/null; then
          open "https://github.com/settings/tokens/new"
        else
          echo "Please open the following URL in your browser to create a new Personal Access Token: "
          echo "  https://github.com/settings/tokens/new"
        fi
        while true; do
          echo "\033[97m"
          read -p "➡️ Paste your Personal Access Token: " YOUR_PERSONAL_GITHUB_ACCESS_TOKEN
          if [[ -n "$YOUR_PERSONAL_GITHUB_ACCESS_TOKEN" ]]; then
            break
          fi
          echo "\033[41mPlease paste your Personal Access Token.\033[0m"
        done
        break
        ;;
      * )
        echo "Invalid choice, please enter 'y' or 'n'."
        ;;
    esac
  done
  echo "\033[0m"

  echo "📝 \033[44mCredentials setting up...\033[0m"

  sleep 1

  cp template-settings.xml settings.xml
  sed 's|'{YOUR_GITHUB_USERNAME}'|'${YOUR_GITHUB_USERNAME}'|g' settings.xml >> temp-settings.xml ; rm settings.xml ; mv temp-settings.xml settings.xml
  sed 's|'{YOUR_PERSONAL_GITHUB_ACCESS_TOKEN}'|'${YOUR_PERSONAL_GITHUB_ACCESS_TOKEN}'|g' settings.xml >> temp-settings.xml ; rm settings.xml ; mv temp-settings.xml settings.xml

  echo "🎉 \033[102mCredentials have been configured!\033[0m"
  echo ""
else
  echo ""
  echo "ℹ️ \033[44mSkipping credentials setup...\033[0m"
  echo ""
fi

./mvnw -s settings.xml clean install -DskipTests
