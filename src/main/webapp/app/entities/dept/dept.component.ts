import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import DeptService from './dept.service';
import { type IDept } from '@/shared/model/dept.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Dept',
  setup() {
    const deptService = inject('deptService', () => new DeptService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const depts: Ref<IDept[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveDepts = async () => {
      isFetching.value = true;
      try {
        const res = await deptService().retrieve();
        depts.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveDepts();
    };

    onMounted(async () => {
      await retrieveDepts();
    });

    const removeId: Ref<string> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IDept) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeDept = async () => {
      try {
        await deptService().delete(removeId.value);
        const message = 'A Dept is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveDepts();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      depts,
      handleSyncList,
      isFetching,
      retrieveDepts,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeDept,
    };
  },
});
