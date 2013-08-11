#!/bin/bash
git pull
mvn clean
rm -R desktop/target
mvn package -Pdesktop
rm -R html/target/webapp
mvn package -Phtml
rm -R android/target
mvn package -Pandroid
rm -R /var/www/drupal/space
cp -R html/target/webapp/ /var/www/drupal/space/
cp android/target/spaceDingus-android.jar /var/www/drupal/space/
cp desktop/target/spaceDingus-desktop-0.1-SNAPSHOT-jar-with-dependencies.jar /var/www/drupal/space/
