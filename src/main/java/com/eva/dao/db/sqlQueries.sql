DELETE FROM eva_test.product WHERE id!='10000000';
SELECT count(*) FROM eva_test.product;
drop table eva_test.product
drop table eva_test.hibernate_sequence